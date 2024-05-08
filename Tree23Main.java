/*
 * Modified from: http://www.java2s.com/ref/java/java-data-structures-234-tree.html
 * 
*/

class DataItem {
   public long dData;

   public DataItem(long dd) {
      dData = dd;
   }

   public void displayItem() {
      System.out.print("/" + dData);
   }
}

class Node {
   private static final int ORDER = 4;
   private int numItems;
   private Node parent;
   private Node childArray[] = new Node[ORDER];
   private DataItem itemArray[] = new DataItem[ORDER - 1];

   public void connectChild(int childNum, Node child) {
      childArray[childNum] = child;
      if (child != null)
         child.parent = this;
   }

   public Node disconnectChild(int childNum) {
      Node tempNode = childArray[childNum];
      childArray[childNum] = null;
      return tempNode;
   }

   public Node getChild(int childNum) {
      return childArray[childNum];
   }

   public Node getParent() {
      return parent;
   }

   public boolean isLeaf() {
      return (childArray[0] == null) ? true : false;
   }

   public int getNumItems() {
      return numItems;
   }

   public DataItem getItem(int index) // get DataItem at index
   {
      return itemArray[index];
   }

   public boolean isFull() {
      return (numItems == ORDER - 1) ? true : false;
   }

   public int findItem(long key)
   {
      for (int j = 0; j < ORDER - 1; j++) 
      {
         if (itemArray[j] == null) 
            break;
         else if (itemArray[j].dData == key)
            return j;
      }
      return -1;
   }

   public int insertItem(DataItem newItem) {
      numItems++;
      long newKey = newItem.dData;

      for (int j = ORDER - 2; j >= 0; j--) {
         if (itemArray[j] == null)
            continue;
         else {
            long itsKey = itemArray[j].dData;
            if (newKey < itsKey)
               itemArray[j + 1] = itemArray[j];
            else {
               itemArray[j + 1] = newItem;
               return j + 1;
            }
         }
      }
      itemArray[0] = newItem;
      return 0;
   }

   public DataItem removeItem() {
      DataItem temp = itemArray[numItems - 1];
      itemArray[numItems - 1] = null;
      numItems--;
      return temp;
   }

   public void displayNode() {
      for (int j = 0; j < numItems; j++)
         itemArray[j].displayItem();
      System.out.println("/");
   }

}

class Tree23 {
   private Node root = new Node();

   public int find(long key) {
      Node curNode = root;
      int childNumber;
      while (true) {
         if ((childNumber = curNode.findItem(key)) != -1)
            return childNumber; 
         else if (curNode.isLeaf())
            return -1; 
         else
            curNode = getNextChild(curNode, key);
      } 
   }
   public void insert(long dValue) {
      Node curNode = root;
      DataItem tempItem = new DataItem(dValue);
      
      while (true) {
         if (curNode.isLeaf())
            break;
         else
            curNode = getNextChild(curNode, dValue);
      }
      curNode.insertItem(tempItem);
      // dilanggar dahulu max items per node dari 2 menjadi 3 
      if (curNode.isFull()) 
      {
        split(curNode); // split it
        curNode = curNode.getParent();
        // untuk setiap node yang melanggar max items per node
        while(curNode.isFull()){
            split(curNode); // split it
            curNode = curNode.getParent();
        }
      }
   }

   public void delete(long dValue){
      Node curNode = root;
      Node sibNode = getNextChild(curNode, dValue);
      Node parNode = curNode.getParent();
      Node chiNode = parNode.getChild(0);
      Node left = null;
      Node right = null;

      while (true) {
         if (curNode.isLeaf())
            break;
         else
            curNode = getNextChild(curNode, dValue);
      }
      // Key pada leaf node yang penuh
      if (curNode.isFull()) {
         curNode.removeItem();
      }
      // Key pada leaf node yang tidak penuh, sibling node penuh
      else {
         while (true) {
         if (sibNode.isLeaf())
            break;
         else
            sibNode = getNextChild(curNode, dValue);
      }
         if (sibNode == null) {
            parNode = curNode.getParent();
            curNode = parNode;
            parNode.removeItem();
            parNode = parNode.connectChild(0, sibNode);
         }
   
      // Key pada leaf node yang tidak penuh, sibling node tidak penuh
         else if (sibNode != null) {
            parNode = curNode.getParent();
            curNode = parNode;
            parNode.removeItem();
            if (parNode == null) {
               parNode = parNode.getChild(0);
            }
            else if (left == null) {
               chiNode = parNode.getChild(0);
               parNode = parNode.connectChild(0, chiNode);
            }
         }
      }
   }
   public void split(Node thisNode) {
      DataItem itemB, itemC;
      Node parent, child2, child3;
      int itemIndex;

      itemC = thisNode.removeItem();
      itemB = thisNode.removeItem();
      child2 = thisNode.disconnectChild(2);
      child3 = thisNode.disconnectChild(3);
      
      Node newRight = new Node();

      if (thisNode == root) {
         root = new Node();
         parent = root;
         root.connectChild(0, thisNode);
      } else
         parent = thisNode.getParent();

      itemIndex = parent.insertItem(itemB);
      int n = parent.getNumItems();

      for (int j = n - 1; j > itemIndex; j--) {
         Node temp = parent.disconnectChild(j);
         parent.connectChild(j + 1, temp);
      }
      parent.connectChild(itemIndex + 1, newRight);
      newRight.insertItem(itemC);
      newRight.connectChild(0, child2);
      newRight.connectChild(1, child3);
   }

   public Node getNextChild(Node theNode, long theValue) {
      int j;

      int numItems = theNode.getNumItems();
      for (j = 0; j < numItems; j++) {
         if (theValue < theNode.getItem(j).dData)
            return theNode.getChild(j);
      }
      return theNode.getChild(j);
   }

   public void displayTree() {
      recDisplayTree(root, 0, 0);
   }

   private void recDisplayTree(Node thisNode, int level, int childNumber) {
      System.out.print("level=" + level + " child=" + childNumber + " ");
      thisNode.displayNode(); 

      int numItems = thisNode.getNumItems();
      for (int j = 0; j < numItems + 1; j++) {
         Node nextNode = thisNode.getChild(j);
         if (nextNode != null)
            recDisplayTree(nextNode, level + 1, j);
         else
            return;
      }
   }

}

public class Tree23Main {
   public static void main(String[] args) {
      Tree23 theTree = new Tree23();

      theTree.insert(50);
      theTree.insert(40);
      theTree.insert(60);
      theTree.insert(30);
      theTree.insert(70);

      theTree.insert(21);
      theTree.insert(19);
      theTree.insert(65);
      theTree.insert(35);
      theTree.insert(18);
      theTree.insert(17);
      theTree.displayTree();
      
      int found = theTree.find(40);
      if (found != -1)
         System.out.println("40 ditemukan di tree");
      else
         System.out.println("40 tidak ditemukan di tree ");
   }
}