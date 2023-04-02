public class TreeLocatorMap<K extends Comparable<K>> implements LocatorMap<K> {

	Map<K, Location> map;
	Locator<K> locator;
	
	public TreeLocatorMap() {
		map = new BST<K,Location>();
		locator = new TreeLocator<K>();
	}
	
	@Override
	public Map<K, Location> getMap() {
		return map;
	}

	@Override
	public Locator<K> getLocator() {
		return locator;
	}

	@Override
	/*public Pair<Boolean, Integer> add(K k, Location loc) {
		Pair<Boolean , Integer> bstPair = map.insert(k, loc);
		if(bstPair.first) {
			locator.add(k, loc);
			return new Pair<Boolean,Integer> (true,bstPair.second);
		}
		else {
			return new Pair<Boolean,Integer>(false,bstPair.second);
		}
	}*/
	public Pair<Boolean, Integer> add(K k, Location loc) {
		Pair<Boolean , Integer> bstPair = map.find(k);
		if(!bstPair.first) {
			if(map.insert(k, loc).first) {
		        locator.add(k, loc);
			    return new Pair<Boolean,Integer> (true,bstPair.second);
			}
		}
	    return new Pair<Boolean,Integer>(false,bstPair.second);
	}

	@Override
	public Pair<Boolean, Integer> move(K k, Location loc) {
		Pair<Boolean, Integer> p1 = map.find(k);
		Pair<Boolean, Integer> locRemove = locator.remove(k, map.retrieve());
		Pair<Boolean,Integer> bstPair = map.remove(k);
		if(bstPair.first) {
			if(locRemove.first) {
				Pair<Boolean,Integer> addPair = add(k,loc);
				    if(addPair.first)
				        return new Pair<Boolean,Integer>(true,bstPair.second);
			}
		}
		return new Pair<Boolean,Integer>(false,bstPair.second);
	}

	@Override
	public Pair<Location, Integer> getLoc(K k) {
		Pair<Boolean,Integer> findK = map.find(k);
		if(findK.first) {
			return new Pair<Location,Integer>(map.retrieve(),findK.second);
		}
		return new Pair<Location,Integer>(null,findK.second);
	}

	@Override
	public Pair<Boolean, Integer> remove(K k) {
		Pair<Boolean, Integer> findK = map.find(k);
		Pair<Boolean, Integer> locRemove = locator.remove(k, map.retrieve());
		Pair<Boolean,Integer> bstPair = map.remove(k);
		if(bstPair.first) {
			if(locRemove.first) 
			     return new Pair<Boolean,Integer>(true,bstPair.second);
		}
		return new Pair<Boolean,Integer>(false,bstPair.second);
	}

	@Override
	public List<K> getAll() {
		return map.getAll();
	}

	@Override
	public Pair<List<K>, Integer> getInRange(Location lowerLeft, Location upperRight) {
		Pair<List<Pair<Location, List<K>>>, Integer> pair = locator.inRange(lowerLeft, upperRight);
		List<K> keysList = new LinkedList<K>();
		if(pair.first.empty()) {
			return new Pair<List<K> , Integer>(keysList , pair.second);
		}
		pair.first.findFirst();
		while(!pair.first.last()) {
			if(!pair.first.retrieve().second.empty()) {
				pair.first.retrieve().second.findFirst();
			    while(!pair.first.retrieve().second.last()) {
				    keysList.insert(pair.first.retrieve().second.retrieve()); 
				    pair.first.retrieve().second.findNext();
			    }
			    keysList.insert(pair.first.retrieve().second.retrieve());
			}
			pair.first.findNext();
		}
		if(!pair.first.retrieve().second.empty()) {
			pair.first.retrieve().second.findFirst();
		    while(!pair.first.retrieve().second.last()) {
			    keysList.insert(pair.first.retrieve().second.retrieve()); 
			    pair.first.retrieve().second.findNext();
		    }
		    keysList.insert(pair.first.retrieve().second.retrieve());
		}
		return new Pair<List<K> , Integer>(keysList , pair.second);
	}
}
