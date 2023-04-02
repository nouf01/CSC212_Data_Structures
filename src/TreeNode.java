public class TreeNode<T> {
    Location loc;
    List<T> data;
    TreeNode<T> c1;
    TreeNode<T> c2;
    TreeNode<T> c3;
    TreeNode<T> c4;
    	
    public TreeNode(Location lo) {
    	loc = lo;
    	c1 = c2 = c3 = c4 = null;
    	data = new LinkedList<T>();
    }
}
