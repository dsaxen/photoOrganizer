package album;

import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public class FlaggedAlbum extends SearchAlbum{
	String albumName;
	
	public FlaggedAlbum(String albumName){
		super(albumName);
		photos=new HashSet<Photo>(); //photo set from searchalbum.
	}
	
	@Override
	public Set<Photo> getPhotos() {
		return photos;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		notify((Photo)arg);
	}
	@Override
	public boolean searchCriteria(Photo p){
		return p.getFlagged();
	}

	@Override
	public void registerParentAlbum(Album a) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteAllSubAlbumsToThisAlbum() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Album getParentAlbum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends AlbumInterface> descendants() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends AlbumInterface> ancestors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parentAlbum(AlbumInterface albumInterface) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPhoto(Photo p) {
		// TODO Auto-generated method stub
		
	}
}
