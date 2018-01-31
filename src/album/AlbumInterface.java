package album;

import java.util.Collection;
import java.util.Set;

public interface AlbumInterface{


	Set<Photo>getPhotos();
	
	public void registerParentAlbum(Album a);
	public Album getParentAlbum();
	
	String toString();
	void deleteAllSubAlbumsToThisAlbum();
	Collection<? extends AlbumInterface> descendants();
	Collection<? extends AlbumInterface> ancestors();
	void parentAlbum(AlbumInterface albumInterface);

	void addPhoto(Photo p);
	
}
