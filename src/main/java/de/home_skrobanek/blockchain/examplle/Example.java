package de.home_skrobanek.blockchain.examplle;

import de.home_skrobanek.blockchain.Block;
import de.home_skrobanek.blockchain.Blockchain;
import de.home_skrobanek.blockchain.serialization.FileHandler;

public class Example {

    public static void main(String[]args){
        //create a new blockchain
        Blockchain chain = new Blockchain(new Block(0, "Start of Blockchain"));

        //add new blocks to the blockchain. Its important to have unique id's !!!!
        chain.addBlock(new Block(1,"Hier g den zweiten Test"));
        chain.addBlock(new Block(2,"Hier gibt es den dritten Test"));
        chain.addBlock(new Block(3,"Hier gibt es den zweiten Test"));
        chain.addBlock(new Block(4,"Hier gibt es den zweiten Test"));

        //with that you can print the complete blockchain in its current state
        chain.printChain();

        //This is usefull if you want to save the blockchain in files.
        FileHandler fh = new FileHandler();
        fh.writeToFile("blockchain.json",chain);
    }
}
