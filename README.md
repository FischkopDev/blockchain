<h1>A little Blockchain project</h1>
<p>This project is a small blockchain. You will be able to store data and check if 
the blockchain is still valid or not. Furthermore you have useful tools for administration
and further development. E.g. Saving the blockchain in a file, creating blocks, hashing,...<br>
Now I'm gonna show you how to work with this project: For now just include this project to your 
work. Below I have some examples of how this blockchain can be used.<br><br>
</p>

<h3>Setup and general information</h3>
<p>In my code you will find a file called Example.java. This complete project isn't that big right now, so
its an example with about 20-30 lines of code.</p>

<h3>Dependencies</h3>
<p>For this project I just used the google Gson library to serialize the data. The hole project is using maven 
for dependencies-Management.</p>

````maven
    <dependencies>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.9</version>
        </dependency>
    </dependencies>
`````

<h3>Some Code as example</h3>
````java
        //Create the blockchain.
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
        
        //Read a blockchain from files
        Blockchain readChain = fh.readFromFile("blockchain.json");
````

<h3>Author</h3>
<p>With this link you will find further information about this project. I will show some more details about how I designed this 
project and what my further plans are.</p>
<br>
<p>Timo Skrobanek<br>
timo@home-skrobanek.de</p>