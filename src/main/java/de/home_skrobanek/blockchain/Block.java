/**
 * @description
 *  In this Block class you'll find everything, what is going
 *  to be stored in the blockchain.
 */
package de.home_skrobanek.blockchain;

public class Block {

    private int id;
    private String data;
    private String dataHash;
    private String lastHash;

    public Block(int id, String data) {
        this.id = id;
        this.data = data;
        this.dataHash = Hash.createSHAHash(id + " " + data + " " + lastHash);

    }

    public String getPrintableData(){
        return "Block " + id+ "\n" + "Data: \t\t" + data + "\nHash: \t\t" + dataHash + "\nlastHash: \t" + lastHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getLastHash() {
        return lastHash;
    }

    public void setLastHash(String lastHash) {
        this.lastHash = lastHash;
    }
}
