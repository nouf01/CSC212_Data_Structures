public class TreeLocator<T> implements Locator<T> {

	TreeNode<T> root, current;
	
	public TreeLocator() {
		root = current = null;
	}
	// Inserts e at location loc and returns the number of comparisons made when
	// searching for loc.
	public int add(T e, Location loc) {
		int num =0;
		TreeNode<T> p ;
		Pair<Boolean,Integer> pir = findKey(loc);
		num = pir.second;
		if(pir.first) {
			current.data.insert(e);
			return num;
		}
		if(loc == null)
			return num;
		p = new TreeNode<T>(loc);
		p.data.insert(e);
		if(root == null) {
			root = current =p;
			return num;
		}
		else if(loc.x < current.loc.x && loc.y <= current.loc.y) {
			//num ++;
			current.c1  =p;
		}
		else if(loc.x <= current.loc.x && loc.y > current.loc.y) {
			//num++;
			current.c2  =p;
		}
		else if(loc.x > current.loc.x && loc.y >= current.loc.y) {
			//num++;
			current.c3  =p;
		}
		else if(loc.x >= current.loc.x && loc.y < current.loc.y) {
			//num++;
			current.c4  =p;
		}
		current =p;
		return num;
	}
	
	private Pair<Boolean,Integer> findKey(Location loc) {
		int num = 0;
		TreeNode<T> p = root,q = root;
		if(root == null || loc == null) {
			return new Pair<Boolean,Integer>(false,num);
		}
		while(p != null) {
			q = p;
			if(p.loc.x == loc.x && p.loc.y == loc.y) {
				current = p;
				num++;
				return new Pair<Boolean,Integer>(true,num);
			}
			else if(loc.x < p.loc.x && loc.y <= p.loc.y) {
				num++;
				p = p.c1;
			}
			else if(loc.x <= p.loc.x && loc.y > p.loc.y) {
				num++;
				p = p.c2;
			}
			else if(loc.x > p.loc.x && loc.y >= p.loc.y) {
				num++;
				p = p.c3;
			}
			else if(loc.x >= p.loc.x && loc.y < p.loc.y) {
				num++;
				p = p.c4;
			}
		}
		current = q;
		return new Pair<Boolean,Integer>(false,num);
	}

	// The first element of the returned pair is a list containing all elements
	// located at loc. If loc does not exist or has no elements, the returned list
	// is empty. The second element of the pair is the number of comparisons made
	// when searching for loc.
	public Pair<List<T>, Integer> get(Location loc) {
		TreeNode<T> q = current;
		Pair<Boolean, Integer> pir = findKey(loc);
		if (pir.first) {
			if (!current.data.empty()) {
				current.data.findFirst();
				if (current.data.retrieve() != null)
					return new Pair<List<T>, Integer>(current.data, pir.second);
			}
		}
		current = q;
		return new Pair<List<T>, Integer>(new LinkedList<T>(), pir.second);
	}

	// Removes all occurrences of element e from location loc. The first element
	// of the returned pair is true if e is removed and false if loc does not exist
	// or e does not exist in loc. The second element of the pair is the number of
	// comparisons made when searching for loc.
	public Pair<Boolean, Integer> remove(T e, Location loc){
		boolean flag = false;
		TreeNode<T> q = current;
		Pair<Boolean,Integer> pir = findKey(loc);
		if (pir.first) {
			if (!current.data.empty()) {
				current.data.findFirst();
				while (!current.data.last()) {
					if (current.data.retrieve() != null) {
						if (current.data.retrieve().equals(e)) {
							current.data.remove();
							flag = true;
						} else
							current.data.findNext();
					}
				}
				if (current.data.retrieve() != null) {
					if (current.data.retrieve().equals(e)) {
						current.data.remove();
						flag = true;
					}
				}
			}
		}
		current =q;
		return new Pair<Boolean, Integer>(flag,pir.second);
	}
		

	// Returns all locations and the elements they contain.
	public List<Pair<Location, List<T>>> getAll(){
		List<Pair<Location, List<T>>> locList = new LinkedList<Pair<Location, List<T>>>();
		if (root == null)
			return locList;
		traverse(locList,root);
		return locList;
	}
	
	private void traverse(List<Pair<Location, List<T>>> locList, TreeNode<T> root) {
		TreeNode<T> p = root;
		if(p == null)
			return;
		locList.insert(new Pair<Location, List<T>>(p.loc, get(p.loc).first));
		traverse(locList,p.c1);
		traverse(locList,p.c2);
		traverse(locList,p.c3);
		traverse(locList,p.c4);
	}

	// The first element of the returned pair is a list of all locations and their
	// data if they are located within the rectangle specified by its lower left and
	// upper right corners (inclusive of the boundaries). The second element of the
	// pair is the number of comparisons made.
	public Pair<List<Pair<Location, List<T>>>, Integer> inRange(Location lowerLeft, Location upperRight){
		List<Pair<Location, List<T>>> list = new LinkedList<Pair<Location, List<T>>>();
		IntegerWrapper num = new IntegerWrapper(0);
		if(root == null || lowerLeft == null || upperRight == null)
			return new  Pair<List<Pair<Location, List<T>>>, Integer>(list,num.get());
		if(upperRight.x < lowerLeft.x || upperRight.y < lowerLeft.y)
			return new  Pair<List<Pair<Location, List<T>>>, Integer>(list,num.get());
			
		Location upperLeft = new Location(lowerLeft.x , upperRight.y);
		Location lowerRight = new Location(upperRight.x , lowerLeft.y);
		Location [] corner = new Location[4];
		corner[0] = lowerLeft;
		corner[1] = upperLeft;
		corner[2] = upperRight;
		corner[3] = lowerRight;
		inRangeRec(root,list,corner,num);
		return new  Pair<List<Pair<Location, List<T>>>, Integer>(list,num.get());
	}
	
	private void inRangeRec(TreeNode<T> p,List<Pair<Location, List<T>>> list, Location[] corner, IntegerWrapper num) {
		if (p == null)
			return;
		num.set(num.get() + 1);
		if (p.loc.x >= corner[0].x && p.loc.x <= corner[2].x && p.loc.y >= corner[0].y && p.loc.y <= corner[2].y) {
			list.insert(new Pair<Location, List<T>>(p.loc, p.data));
		}
		boolean oneTaken = false;
		boolean twoTaken = false;
		boolean threeTaken = false;
		boolean fourTaken = false;
		for(int i=0; i<4; i++) {
			if(corner[i].x < p.loc.x && corner[i].y <= p.loc.y && !oneTaken) {
				oneTaken = true;
				inRangeRec(p.c1, list, corner, num);
			}
			else if(corner[i].x <= p.loc.x && corner[i].y > p.loc.y && !twoTaken) {
				twoTaken = true;
				inRangeRec(p.c2, list, corner, num);
			}
			else if(corner[i].x > p.loc.x && corner[i].y >= p.loc.y && !threeTaken) {
				threeTaken = true;
				inRangeRec(p.c3, list, corner, num);
			}
			else if(corner[i].x >= p.loc.x && corner[i].y < p.loc.y && !fourTaken) {
				fourTaken = true;
				inRangeRec(p.c4, list,corner, num);
			}
		}
	}


}