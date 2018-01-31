package album;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;



public class Album implements AlbumInterface{

	private Set<AlbumInterface>childAlbums;
	private Set<Photo>photoGroup; //photoSet är ju i princip samma sak som ett album.
	private String albumName;
	private Album parentAlbum;
	private static Set<AlbumInterface>allAlbumsThatAreChildren;
	private static int count=0;

	public Album(String albumName){//child Album 

		//precondition
		assert albumName!=null &&albumName!=""; 

		this.albumName=albumName;
		childAlbums=new HashSet<AlbumInterface>();
		photoGroup=new HashSet<Photo>();

		if (count ==0){
			allAlbumsThatAreChildren=new HashSet<AlbumInterface>();
			count++;
		}

		assert this.albumName!=null;
		assert classInvariant();
	}

	public boolean classInvariant(){//check the things that are before constructor
		if (albumName==(null)){
			return false;
		}
		else if (albumName.equals("")){
			return false;
		}
		else if (childAlbums==(null)){
			return false;
		}
		else if (photoGroup==(null)){ 
			return false;
		}
		else{
			return true;
		}
	}
	public String getAlbumName(){
		return albumName;
	}
	public void setAlbumName(String newName){
		//precondition
		assert classInvariant();
		assert (newName!="")&&(newName!=null); 

		albumName=newName;

		//postcondiion
		assert classInvariant();
		assert albumName!=null&&albumName!="";
	}
	public void addPhoto(Photo p){
		//precondition
		assert classInvariant();
		assert p!=null;

		photoGroup.add(p); 

		//postcondition
		assert classInvariant();
		assertTrue(photoGroup.contains(p));
	} 

	public Set<Photo> getPhotos(){
		return photoGroup;
	}

	public Set<AlbumInterface>getChildAlbums(){
		return childAlbums;
	}
	public void registerChildAlbum(AlbumInterface a){
		//precondition
		assert classInvariant();
		assert !a.equals(this);//kan inte vara child till sig själv 
		
	    childAlbums.add((AlbumInterface) a);
	    a.parentAlbum((AlbumInterface) this); //register parent album (only 1)
	    allAlbumsThatAreChildren.add((AlbumInterface)a);
	    
		//postcondition
		assert classInvariant();
		assert childAlbums.contains(a);
		assert allAlbumsThatAreChildren.contains(a);
	}
	public boolean isChildAlbum(Album a){
		return childAlbums.contains(a);
	}
	public boolean isChildAlbum(){
		return false;
	}
	public Set<AlbumInterface>getAllAlbumsThatAreChildren(){
		return allAlbumsThatAreChildren;
	}

	public AlbumInterface getSubAlbum(String name){//added to task 3
		for (AlbumInterface a:getAllAlbumsThatAreChildren()){ 
			if (a.toString().equals(name)){
				return a;
			}
		}
		return null;
	}
	public Set <AlbumInterface> descendants () {
		Set<AlbumInterface> d = new HashSet < AlbumInterface >() ;
		for ( AlbumInterface p : childAlbums ) {
			d . add (   p ) ;
			if (p.descendants()==null){
				break;
			}
			d . addAll (   p. descendants () ) ;
		}
		return d ;
	}
	public Set<AlbumInterface > ancestors(){
		Set < AlbumInterface > a = new HashSet < AlbumInterface   >() ;
		return a ;
	}



	public boolean nameAlreadyExists(String name){
		Iterator<AlbumInterface> iterator = descendants().iterator();
		while (iterator.hasNext()){
			AlbumInterface a=iterator.next();
			if (a.toString().equals(name)){
				return true;
			}
		}
		return false;
	}


	public Set<AlbumInterface>allSubAlbums(){//all albums that are subAlbums to the album object
		assert classInvariant();
		Set<AlbumInterface>c=new HashSet<AlbumInterface>();
		for (AlbumInterface a:childAlbums){ 
			//precondition
			assert a!=null;
			c.add(a);
		}

		assert classInvariant();
		return c;
	}
	public int getAlbumsPhotoAmount() {//3 pure methods, just the invariant to check null values
		return photoGroup.size(); 
	}
	public int getChildAlbumSize(){
		return childAlbums.size();
	}
	public void deleteSubAlbum(String name){ //deletes an album from the root album as specified in the photoOrganizer class.
		Iterator<AlbumInterface> iterator = childAlbums.iterator();
		while (iterator.hasNext()){
			AlbumInterface a=iterator.next();
			if (a.toString().equals(name)){
				iterator.remove();
			}
		}
	}

	public void deleteAllSubAlbumsToThisAlbum() {
		if (childAlbums.size()==0){
			return;
		}
		Iterator<AlbumInterface> iterator = childAlbums.iterator();
		while (iterator.hasNext()){
			@SuppressWarnings("unused")
			AlbumInterface a= iterator.next();
			iterator.remove();//to avoid concurrentmodificationexception.
		}
	}

	public Set<Photo> setPhotos() {
		// TODO Auto-generated method stub
		return null;
	}
	public String toString(){
		return this.albumName;
	}

	@Override
	public void registerParentAlbum(Album a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Album getParentAlbum() {
		System.out.println(this);
		System.out.println(parentAlbum);
		return parentAlbum;
	}
	public void parentAlbum(Album parentAlbum){
		this.parentAlbum=parentAlbum;
	}

	@Override
	public void parentAlbum(AlbumInterface parentAlbum) {
		this.parentAlbum=(Album) parentAlbum;
		
	}



}
