import java.util.ArrayList;

public class LinkStrand implements IDnaStrand{
    private Node myFirst, myLast;
    private long mySize;
    private int myAppends;
    private int myIndex;
    private Node myCurrent;
    private int myLocalIndex;
    private StringBuilder myInfo;

    private class Node {
        String info;
        Node next;
        Node(String x){
            info = x;
        }
        Node(String x, Node node){
            info = x;
        next = node;
        }
    }
    public LinkStrand(){
        this("");
    }
    public LinkStrand(String info){
        initialize(info);
    }
    @Override
    public long size() {
        // TODO Auto-generated method stub
        return mySize;
    }

    @Override
    public void initialize(String source) {
        // TODO Auto-generated method stub
        myFirst = new Node(source);
        myLast = myFirst;
        myCurrent = myFirst;
        myIndex = 0;
        myLocalIndex = 0;
        mySize = source.length();
        myAppends = 0;
    }

    @Override
    public IDnaStrand getInstance(String source) {
        // TODO Auto-generated method stub
        return new LinkStrand(source);
    }

    @Override
    public IDnaStrand append(String dna)
	{
		myLast.next = new Node(dna);
		myLast = myLast.next;
		mySize += myLast.info.length();
		myAppends ++;
		return this;
	}
     private void appendToFront(String dna)
	{
		Node guy = new Node(dna);
		this.mySize += guy.info.length();
		guy.next = myFirst;
		myFirst = guy;
	}
    @Override
    public String toString(){
        Node hold = myFirst;
        StringBuilder sub = new StringBuilder();
        while (hold.next != null){
            sub.append(hold.info);
            hold = hold.next;
        }
        sub.append(hold.info);
        return sub.toString();
    }

    @Override
    public IDnaStrand reverse() {
        // TODO Auto-generated method stub
        Node sub = myFirst;
		LinkStrand temp = new LinkStrand();
		while (sub != null) {
			StringBuilder guy2 = new StringBuilder(sub.info);
			String x = guy2.reverse().toString();
			temp.appendToFront(x);
			sub = sub.next;	
		}
		return temp;
	}		

    @Override
    public int getAppendCount() {
        // TODO Auto-generated method stub
        return myAppends;
    }

    @Override
    public char charAt(int index) {
        // TODO Auto-generated method stub
        if(this.size() <= myIndex){
            return Character.MIN_VALUE;
        }
        if (index<0){
            return Character.MIN_VALUE;
        }
        if (myIndex >= index){
            myIndex = 0;
            myCurrent = myFirst;
            myLocalIndex = 0;
        }
        while (index != myIndex){
			myIndex += 1;
			myLocalIndex += 1;
			if (myCurrent.next != null && myCurrent.info.length()<= myLocalIndex) {
				myCurrent = myCurrent.next;
				myLocalIndex = 0;
			}	
		}
		return myCurrent.info.charAt(myLocalIndex);
    }
}
