package slideShow;

import java.util.Iterator;
import java.util.Set;

import album.AlbumInterface;
import album.Photo;

public class SlideShowController { //all methods must be public because they are called by SlideSlowWindow.

	private boolean fadedIn=false;//assume that picture must be first faded in.
	private boolean pause=false;//assume the you dont want pause first.
	private AlbumInterface a;
	private SlideShowView v;
	private float alphaCoefficient;
	private int tick;
	private int tick2;
	private Iterator<Photo> iterator;
	private Set<Photo>photoSet;//get the photos in album
	private boolean albumNameHidden=false;

	public SlideShowController(AlbumInterface root, SlideShowView v) {
		this.a=root;
		this.v=v;
		photoSet=root.getPhotos();
		tick=175; //show the first picture immediately, which means that tick must be put to 175.
		tick2=0;
		
	}
	public void timeTick() {// 2 seconds fade in, 3 seconds show photo with no fade (7 secs total), 2 seconds fade out


		if (pause==false){ //the biggest state with active is pause.
			if (fadedIn==false){//fade in 50*40ms = 2s. 
				if(alphaCoefficient+0.02f > 1){
					alphaCoefficient = 1;
				}
				else{
					alphaCoefficient=alphaCoefficient+0.02f;
				}
				v.setAlpha(alphaCoefficient);
				if (alphaCoefficient==1){
					fadedIn=true; 
				}
			}
			if(!iterator.hasNext()){//if no more photos are to be shown, the slide show begins again.
				iterator=photoSet.iterator();
			}
			if(tick > 175){//when tick has reached 75, it means that the method has been called 75 times, which means 3 seconds has passed
				v.setPhoto(iterator.next());//put new photo to be displayed.
				tick=0;
			}
			tick++; //increments to check when method is called 75 times
			tick2++; //increments to check when 10 seconds has passed to remove album name from slide.

			if (tick2>250&&albumNameHidden==false){
				v.setText("");
				v.repaint();
				albumNameHidden=true;
			}
			

			if (fadedIn==true&&tick>125){//fade out 2 seconds. The 125 means that 5 seconds have passed from the beginning of the fade in.
				if(alphaCoefficient-0.02f < 0){
					alphaCoefficient = 0;
				}
				else{
					alphaCoefficient=alphaCoefficient-0.02f;
				}
				v.setAlpha(alphaCoefficient);
				if (alphaCoefficient==0){
					fadedIn=false;
				}
			}
		}
	}

	public void startPressed() {
		
		iterator=photoSet.iterator(); //putting the correct album to iterator.
		alphaCoefficient=0;
		tick=175;
		tick2=0;
		v.setText(a.toString());
		albumNameHidden=false;

	}

	public void nextSlidePressed() {
		alphaCoefficient=0;//the fade in should take place.
		tick=175; //messages the tick method that it can display the next photo (Make the program "believe" that 7 seconds has passed)
	}

	public void pauseUnpausePressed() {
		if (pause==false){//if you want to pause
			pause=true;//now the program is paused.
		}
		else if (pause==true){//if you want to unpause
			pause=false;
		}
	}
}
