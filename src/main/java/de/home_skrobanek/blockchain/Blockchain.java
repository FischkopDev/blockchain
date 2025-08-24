/**
 * @author
 *  Timo Skrobanek
 *
 * @date
 *  23.06.2022
 *
 * @description
 *  This class will be the setup for a blockchain.
 *  To work with this blockchain you need to use
 *  unique ids for every Block, otherwise it won't work
 *  to add this block to the Blockchain.
 */
package de.home_skrobanek.blockchain;


import de.home_skrobanek.blockchain.currency.Transaction;
import de.home_skrobanek.blockchain.serialization.FileHandler;

import java.util.*;

public class Blockchain {

    private final List<Block> chain = new ArrayList<>();
    private final TransactionPool txPool = new TransactionPool();
    private final Map<String, Double> accounts = new HashMap<>();
    private final double miningReward = 10.0;
    private static final String SYSTEM = "SYSTEM";


    public Blockchain() {
        // Genesis-Block
        Block genesis = new Block(0, Collections.emptyList(), "Start of the journey");
        chain.add(genesis);
        // Optional: create SYSTEM account
        accounts.put(SYSTEM, Double.MAX_VALUE); // SYSTEM hat "unbegrenzte" Mittel für Start-giveaways
    }

    // accounts verwalten
    public void addAccount(String owner) {
        addAccount(owner, 0.0);
    }

    public void addAccount(String owner, double startBalance) {
        if (!accounts.containsKey(owner)) {
            accounts.put(owner, 0.0);
            if (startBalance > 0) {
                // Startguthaben via SYSTEM-Transaktion in einen neuen Block einfügen
                Transaction t = new Transaction(SYSTEM, owner, startBalance);
                txPool.addTransaction(t);
                // wir mine direkt den Block, damit das Startguthaben bestätigt ist
                minePendingTransactions("miner_temp_for_account_setup");
            }
        }
    }

    // Transaktion an Pool anhängen (prüft Saldo des Senders)
    public boolean addTransaction(Transaction tx) {
        if (tx.getSender().equals(SYSTEM)) {
            // SYSTEM-Transaktionen sind immer erlaubt
            txPool.addTransaction(tx);
            return true;
        }
        double senderBalance = getBalance(tx.getSender());
        if (senderBalance >= tx.getAmount()) {
            // Empfänger ggf. automatisch anlegen (balance 0)
            accounts.putIfAbsent(tx.getReceiver(), 0.0);
            accounts.putIfAbsent(tx.getSender(), 0.0);
            txPool.addTransaction(tx);
            return true;
        } else {
            return false;
        }
    }

    // Mining: alle pending tx -> neuer Block; reward an miner
    public void minePendingTransactions(String minerAddress) {
        // reward tx
        Transaction rewardTx = new Transaction(SYSTEM, minerAddress, miningReward);
        // Sammle alle pending + reward
        List<Transaction> toInclude = new ArrayList<>(txPool.drain());
        toInclude.add(rewardTx);

        Block newBlock = new Block(chain.size(), toInclude, getLatestBlock().getHash());
        chain.add(newBlock);
        // (Option) Aktualisiere accounts-Map nicht nötig, da getBalance aus chain berechnet.
        accounts.putIfAbsent(minerAddress, 0.0);
    }

    public double getBalance(String owner) {
        double balance = accounts.getOrDefault(owner, 0.0);
        // Wenn wir Salden persistieren würden, müssten wir sie hier berücksichtigen.
        // Stattdessen berechnen wir aus der Chain (in Summe) und addieren Start/initialer Map-Wert.
        // Startwert (accounts map) enthält nur initiale Top-ups, sonst 0.
        double computed = 0.0;
        for (Block b : chain) {
            for (Transaction t : b.getTransactions()) {
                if (owner.equals(t.getReceiver())) computed += t.getAmount();
                if (owner.equals(t.getSender())) computed -= t.getAmount();
            }
        }
        // accounts map kann initiale Startbalance/andere Off-Chain-Werte enthalten
        // um einfache Startguthaben zu unterstützen, addiere initial value:
        double initial = accounts.getOrDefault(owner, 0.0);
        // Wenn initial durch Chain schon berücksichtigt wurde (z.B. SYSTEM -> owner), dann wäre initial 0.
        // Um doppelte Zählung zu vermeiden: wir designen so, dass initial nur default ist (0) oder verwendet
        // wird für persistent off-chain. Für dieses Beispiel addieren wir nur initial wenn >0 und chain doesn't include it.
        // Simpler Ansatz: return computed (Chain ist Source-of-Truth).
        return computed;
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public boolean isChainValid() {
        // Einfacher Validitätscheck: Hash-Integrität und lastHash-Verknüpfung
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            if (!current.getLastHash().equals(previous.getHash())) {
                System.out.println("Invalid link at index " + i);
                return false;
            }
            // recompute hash and compare
            // (Hash ist final im Block; um neu zu berechnen wäre calculateHash method non-public — wir rely darauf)
            // Für Robustheit könnte man Block.calculateHash() öffentlich machen und vergleichen.
        }
        return true;
    }

    /*
        Are the two chains the same? Or is one trying to do something different
     */
    public boolean isSameAs(Blockchain other) {
        if (this.chain.size() != other.chain.size()) {
            return false;
        }

        for (int i = 0; i < chain.size(); i++) {
            Block thisBlock = this.chain.get(i);
            Block otherBlock = other.chain.get(i);

            // Prüfe Hashes
            if (!thisBlock.getHash().equals(otherBlock.getHash())) {
                return false;
            }

            // Prüfe lastHash
            if (!thisBlock.getLastHash().equals(otherBlock.getLastHash())) {
                return false;
            }

            // Prüfe Transaktionslisten
            List<Transaction> thisTxs = thisBlock.getTransactions();
            List<Transaction> otherTxs = otherBlock.getTransactions();
            if (thisTxs.size() != otherTxs.size()) {
                return false;
            }
            for (int t = 0; t < thisTxs.size(); t++) {
                Transaction tx1 = thisTxs.get(t);
                Transaction tx2 = otherTxs.get(t);
                if (!tx1.toString().equals(tx2.toString())) { // einfache Vergleichsmethode
                    return false;
                }
            }
        }
        return true;
    }


    // Debug / Info
    public void printChain() {
        for (Block b : chain) {
            System.out.println("Block " + b.getIndex() + " hash: " + b.getHash() + " last: " + b.getLastHash());
            for (Transaction t : b.getTransactions()) {
                System.out.println("  tx: " + t);
            }
        }
    }

    private static void printBalances(Blockchain bc, String... accounts) {
        for (String a : accounts) {
            System.out.printf("%s: %.2f\n", a, bc.getBalance(a));
        }
    }

    public static void main(String[] args) {
        Blockchain bc = new Blockchain();

        // Accounts anlegen (automatisch bei Bedarf auch möglich)
        bc.addAccount("alice", 100.0);  // Startguthaben von 100
        bc.addAccount("bob", 50.0);     // Startguthaben von 50
        bc.addAccount("miner", 0.0);    // Miner-Konto

        bc.addTransaction(new Transaction("alice", "bob", 30.0));
        bc.minePendingTransactions("miner");

        Blockchain bc2 = new Blockchain();

        bc2.addAccount("alice", 100.0);  // Startguthaben von 100
        bc2.addAccount("bob", 50.0);     // Startguthaben von 50
        bc2.addAccount("miner", 0.0);    // Miner-Konto

        bc2.addTransaction(new Transaction("alice", "bob", 30.0));
        bc2.minePendingTransactions("miner");

        // Chain-Integrity prüfen
        System.out.println("\nBlockchain valid? " + bc.isChainValid());
        System.out.println("Second one valid? " + bc2.isChainValid());

        System.out.println("Are the chains the same: " + bc.getLatestBlock().getHash());
        System.out.println(bc2.getLatestBlock().getHash());

        FileHandler handler = new FileHandler();
        handler.writeToFile("blockchain.json", bc);
    }


    class TransactionPool {
        private final List<Transaction> pending = new ArrayList<>();

        public synchronized void addTransaction(Transaction tx) {
            pending.add(tx);
        }

        public synchronized List<Transaction> drain() {
            List<Transaction> copy = new ArrayList<>(pending);
            pending.clear();
            return copy;
        }

        public synchronized List<Transaction> peekAll() {
            return Collections.unmodifiableList(new ArrayList<>(pending));
        }

        public synchronized void clear() { pending.clear(); }
    }

}
