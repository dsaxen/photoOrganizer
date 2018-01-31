package album;

import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public class GreatAlbum extends SearchAlbum{

	String greatAlbumName;

	public GreatAlbum(String albumName){
		super(albumName);
		photos=new HashSet<Photo>();
	}

	public String getAlbumName() {
		return greatAlbumName;
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
		if (p.getRating()>=4){
			return true;
		}
		return false;
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
