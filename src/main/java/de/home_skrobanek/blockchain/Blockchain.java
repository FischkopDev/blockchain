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

import java.util.ArrayList;

public class Blockchain {

    private ArrayList<Block> chain;

    public Blockchain(Block block){
        chain = new ArrayList<>();
        chain.add(block);

    }

    /*
        Add a new Block to our chain. Furthermore,
        check if this block already exists and use
        the data-hash of our last block.
     */
    public boolean addBlock(Block b){
        if(!existsBlock(b)) {
            Block last = getLastBlock();
            b.setLastHash(last.getDataHash());

            chain.add(b);
            return true;
        }
        return false;
    }

    /*
        Check if a specific block is already added
        to our blockchain.
     */
    public boolean existsBlock(Block b){
        for(int i = 0; i < chain.size(); i++){
            if(chain.get(i).getId() == b.getId())
                return true;
        }
        return false;
    }

    /*
        This method will provid the last Block of
        our blockchain.
     */
    public Block getLastBlock(){
        return chain.get(chain.size()-1);
    }

    /*
        Check another blockchain, if the data are valid.
        You will not get a prov that your blockchain is correct.
        After this method you have to check which blockchain is
        invalid.
     */
    public boolean verifyCompleteBlockchain(Blockchain otherChain){
        if(chain.size() == otherChain.getChain().size()){
            for(int i = 0; i < chain.size(); i++) {
                if(chain.get(i).getDataHash() != chain.get(i).getDataHash())
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    /*
        Verify your blockchain without the last @withoutLast Blocks.
     */
    public boolean verifyCompleteBlockchain(int withoutLast, Blockchain otherChain){
        if(chain.size() == otherChain.getChain().size()){
            for(int i = 0; i < chain.size()-withoutLast; i++) {
                if(chain.get(i).getDataHash() != chain.get(i).getDataHash())
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    /*
        Verify the blockchain just with the last block in your
        blockchain.
     */
    public boolean verifyBlockchain(Blockchain otherChain){
        if(chain.size() == otherChain.getChain().size()){
            return getLastBlock().getDataHash().equals(otherChain.getLastBlock().getData());
        }
        else
            return false;
    }

    /*
        Here we provid a method to print all needed data
        of the blockchain with a specific format.
     */
    public void printChain(){
        System.out.println("Output:");
        System.out.println();
        for(Block b : chain){
            System.out.println(b.getPrintableData());
            System.out.println();
            System.out.println("---------------------------------------");
        }
    }

    public ArrayList<Block> getChain() {
        return chain;
    }

    public void setChain(ArrayList<Block> chain) {
        this.chain = chain;
    }

}
