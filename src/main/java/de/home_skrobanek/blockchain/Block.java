/**
 * @description
 *  In this Block class you'll find everything, what is going
 *  to be stored in the blockchain.
 */
package de.home_skrobanek.blockchain;

import de.home_skrobanek.blockchain.currency.Transaction;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Block {
    private final int index;
    private final long timestamp;
    private final List<Transaction> transactions;
    private final String lastHash;
    private final String hash;
    // optional: nonce für Proof-of-Work (hier nicht gebraucht)

    public Block(int index, List<Transaction> transactions, String lastHash) {
        this.index = index;
        this.timestamp = System.currentTimeMillis();
        this.transactions = new ArrayList<>(transactions);
        this.lastHash = lastHash;
        this.hash = calculateHash();
    }

    public int getIndex() { return index; }
    public long getTimestamp() { return timestamp; }
    public List<Transaction> getTransactions() { return Collections.unmodifiableList(transactions); }
    public String getLastHash() { return lastHash; }
    public String getHash() { return hash; }

    private String calculateHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(index).append(timestamp).append(lastHash);
        for (Transaction t : transactions) sb.append(t.toString());
        return sha256(sb.toString());
    }

    private static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}