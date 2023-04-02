
public class BST<K extends Comparable<K>, T> implements Map<K, T> {
	
	BSTNode<K,T> root, current;
	
	public BST() {
		root = current = null;
	}

	@Override
	public boolean empty() {
		return root == null;
	}

	@Override
	public boolean full() {
		return false;
	}

	@Override
	public T retrieve() {
		return current.data;
	}

	@Override
	public void update(T e) {
		current.data = e;
	}

	@Override
	public Pair<Boolean, Integer> find(K key) {
		int num =0;
		boolean found=false;
		BSTNode<K,T> p = root;
		Pair <Boolean,Integer> pr;
		if(empty()) {
			pr = new Pair<Boolean,Integer>(found,num);
			return pr;
		}
		while (p != null) {
			if(p.key.compareTo(key)== 0) {
				num++;
				found = true;
				current = p;
				pr = new Pair<Boolean,Integer>(found,num);
				return pr;
			}
			else if(key.compareTo(p.key)<0) {
				num++;
				p = p.left;
			}
			else {
				num++;
				p = p.right;
			}
		}
		pr = new Pair<Boolean,Integer>(found, num);
		return pr;		
	}

	@Override
	public Pair<Boolean, Integer> insert(K key, T data) {
		int num =0;
		BSTNode<K,T> p = current , t = root, q = root;
		if(key == null)
			return new Pair<Boolean, Integer>(false,num);
		if(empty()) {
			root = current = new BSTNode<K,T>(key,data);
			return new Pair<Boolean, Integer>(true,num);
		}
		else {
			while(t != null) {
				q = t;
				if(key.compareTo(t.key)==0) {
					num++;
					current = p;
					return new Pair<Boolean, Integer>(false,num);
				}
				else if(key.compareTo(q.key) < 0) {
					num++;
					t = t.left;
				}
				else {
					t = t.right;
				}	
			}
			if(key.compareTo(q.key) < 0) {
				num++;
				q.left = new BSTNode<K, T>(key,data);
				current = q;
				return new Pair<Boolean ,Integer>(true,num);
			}
			else {
				q.right = new BSTNode<K, T>(key,data);
				current = q;
				return new Pair<Boolean ,Integer>(true,num);	
			}
			
		}
	}

	@Override
	public Pair<Boolean, Integer> remove(K key) {
		BooleanWrapper removed = new BooleanWrapper(false);
		IntegerWrapper num = new IntegerWrapper(0);
		BSTNode<K,T> p;
		p = remove_aux(key,root,removed,num);
		current = root = p;
		return new Pair<Boolean, Integer> (removed.get(),num.get());
	}

	private BSTNode<K,T> remove_aux(K key, BSTNode<K,T> p, BooleanWrapper flag, IntegerWrapper num){
	    BSTNode<K,T> q,child = null;
	    if(p==null)
	    	return null;
	    if(key.compareTo(p.key) < 0) {
	    	num.set(num.get() + 1) ;
	    	p.left = remove_aux(key,p.left,flag,num);
	    }
	    else if(key.compareTo(p.key) > 0) {
	    	num.set(num.get() + 1) ;
	    	p.right = remove_aux(key,p.right,flag,num);
	    }
	    else {
	    	flag.set(true);
	    	if(p.left != null && p.right != null) {
	    		q = findmin(p.right);
	    		p.key = q.key;
	    		p.data = q.data;
	    		p.right = remove_aux(q.key, p.right, new BooleanWrapper(false), new IntegerWrapper(0));
	    	}
	    	else {
	    		if(p.right == null)
	    			child = p.left;
	    		else if(p.left == null)
	    			child = p.right;
	    		return child;
	    	}
	    }
	    return p;
	}	
	
	private BSTNode<K, T> findmin(BSTNode<K, T> p){
		if(p == null)
			return null;
		
		while(p.left != null){
			p = p.left;
		}
		
		return p;
	}

	@Override
	public List<K> getAll() {
		List<K> listKeys = new LinkedList<K>(); 
		traverse(listKeys , root);
		return listKeys;
	}

	private void traverse(List<K> listKeys, BSTNode<K,T> root) {
		BSTNode<K,T> p = root;
		if(p == null)
			return;
		else if(p.left != null) {
			traverse(listKeys,p.left);
		}
		listKeys.insert(p.key);
		traverse(listKeys,p.right);
	}
	
}
