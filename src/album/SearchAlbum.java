package album;

import java.util.Observer;
import java.util.Set;

abstract class SearchAlbum implements Observer, AlbumInterface {
	String albumName;
	
	
	public SearchAlbum(String albumName) {
		this.albumName=albumName;
	}
	protected Set<Photo>photos;
	protected Album parentAlbum;
	
	public void notify(Photo p){
		if(searchCriteria(p)){
            photos.add(p);
        }
		else{
            if(photos.contains(p)){
                photos.remove(p);
            }
        }
	}
	public boolean searchCriteria(Photo p){
		return false;
	}
	@Override
	public String toString(){
		return albumName;
	}
}
