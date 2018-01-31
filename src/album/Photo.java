package album;
import java.io.File;
import java.util.Observable;

/**
 * Photo is an immutable class representing a digital photo file on disk.
 * 
 * @author jbaek, rcm
 */
public class Photo extends Observable{//observer pattern

	private final File file;
	private boolean flagged=false;
	private int rating=0;

	/**
	 * Make a Photo for a file. Requires file != null.
	 */
	public Photo(File file) {
		this.file = file;
		this.setRating(0);
	}

	/**
	 * @return the file containing this photo.
	 */
	public File getFile() {
		return file;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Photo && ((Photo) obj).file.equals(file);
	}

	@Override
	public int hashCode() {
		return file.hashCode();
	}
	
	public boolean getFlagged(){
		return flagged;
	}
	public void setFlagged(boolean flagged){ //observer updates every time the photo state is changed.
		this.flagged=flagged;
		setChanged();
		notifyObservers(this);
	}
	public int getRating(){
		return rating;
	}
	public void setRating(int rating){
		this.rating=rating;
		setChanged();
		notifyObservers(this);
	}
}
