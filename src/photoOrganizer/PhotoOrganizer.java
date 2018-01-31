package photoOrganizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import album.Album;
import album.AlbumInterface;
import album.FlaggedAlbum;
import album.GreatAlbum;
import album.Photo;
import album.PhotoLoader;
import slideShow.SlideShowWindow;

/**
 * PhotoOrganizer is a window that allows arranging photos into
 * hierarchical albums and viewing the photos in each album.
 * 
 * Original @author rcm
 * 
 * This class is incomplete and it does not compile. You need to edit it 
 * in order to use the class(es) to represent albums that you have created.
 * 
 * You can modify any part of this class. However, we have marked with a TODO 
 * comment tag the sections of code that probably require most of your attention.
 */
public class PhotoOrganizer extends JFrame  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode root;
	private final JTree albumTree;
	private final PreviewPane previewPane;
	private Album rootAlbum;
	private Set<Photo>photos=new HashSet<Photo>();
	private JButton newAlbumButton;
	private JButton deleteAlbumButton;
	private JButton addPhotosButton;
	private JButton removePhotosButton;
	private JButton flagPhotosButton;
	private JButton ratePhotosButton;
	private JButton startSlideShowButton;
	protected boolean addedSearchAlbums=false;
	FlaggedAlbum flaggedAlbum;
	GreatAlbum greatAlbum;

	/**
	 * Main entry point of photo organizer.
	 * @param args command line arguments
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
					// If Nimbus is not available, you can set the GUI to another look and feel.
				}
				PhotoOrganizer main = new PhotoOrganizer();

				main.setVisible(true);

				if (args.length == 0) {
					main.loadPhotos("sample-photos");
				} else if (args.length == 1) {
					main.loadPhotos(args[0]);
				} else {
					System.err.println("too many command-line arguments");
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Make a PhotoOrganizer window.
	 */
	public PhotoOrganizer() {

		root=new DefaultMutableTreeNode("All photos");

		// set up the panel on the left with two subpanels in a vertical layout
		JPanel catalogPanel = new JPanel();
		catalogPanel.setLayout(new BoxLayout(catalogPanel,
				BoxLayout.PAGE_AXIS));


		// make the row of buttons 
		JPanel buttonPanel = makeButtonPanel();
		catalogPanel.add(buttonPanel);
		JPanel anotherButtonPanel=makeAnotherButtonPanel();
		catalogPanel.add(anotherButtonPanel);

		// make the album tree
		albumTree = makeCatalogTree();
		catalogPanel.add(new JScrollPane(albumTree));

		// make the image previewer
		previewPane = new PreviewPane();

		// put the catalog tree and image previewer side by side, 
		// with an adjustable splitter between
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				catalogPanel, previewPane);
		splitPane.setDividerLocation(700);
		this.add(splitPane);

		// give the whole window a good default size
		this.setTitle("Photo Organizer");
		this.setSize(1200,600);

		// end the program when the user presses the window's Close button
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);		
		this.setLocationRelativeTo(null);
	}

	/**
	 * Load the photos found in all subfolders of a path on disk.
	 * If path is not an actual folder on disk, has no effect.
	 */
	public void loadPhotos(String path) {

		rootAlbum=new Album("All photos");


		Set<Photo> photos = PhotoLoader.loadPhotos(path);
		setPhotos(photos);
		Iterator<Photo> iterator = photos.iterator();
		while(iterator.hasNext()) {
			rootAlbum.addPhoto(iterator.next()); //nu har rootAlbum alla foton från samples-photos foldern
		}
		previewPane.setPhotos(getPhotos());
		previewPane.display();


	}

	private void setPhotos(Set<Photo> photos) {
		this.photos=photos;

	}

	/**
	 * Make the button panel for manipulating albums and photos.
	 */

	private JPanel makeAnotherButtonPanel(){
		JPanel panel2 = new JPanel ();

		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));

		JButton addSearchBasedAlbumsButton=new JButton("Include search based albums");
		addSearchBasedAlbumsButton.setBackground(Color.ORANGE);
		addSearchBasedAlbumsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try{
					flaggedAlbum=new FlaggedAlbum("Flagged");
					greatAlbum=new GreatAlbum("Great Photos");


					for (Photo p: rootAlbum.getPhotos()){
						p.addObserver( flaggedAlbum);
						p.addObserver( greatAlbum);
					}

					rootAlbum.registerChildAlbum(flaggedAlbum);
					rootAlbum.registerChildAlbum(greatAlbum);


					DefaultMutableTreeNode trnode= new DefaultMutableTreeNode();
					trnode.setUserObject(new DefaultMutableTreeNode(flaggedAlbum.toString()));
					DefaultTreeModel model = (DefaultTreeModel) (albumTree.getModel());
					model.insertNodeInto(trnode,getSelectedTreeNode(), getSelectedTreeNode().getChildCount());
					albumTree.scrollPathToVisible(new TreePath(trnode.getPath()));

					DefaultMutableTreeNode trnode2= new DefaultMutableTreeNode();
					trnode2.setUserObject(new DefaultMutableTreeNode(greatAlbum.toString()));
					try{
						model.insertNodeInto(trnode2,getSelectedTreeNode(), getSelectedTreeNode().getChildCount());
					}
					catch(NullPointerException ex){
						JOptionPane.showMessageDialog(null, "You must choose the all Photos icon to include search based albums.");
						return;
					}
					albumTree.scrollPathToVisible(new TreePath(trnode.getPath()));
					addSearchBasedAlbumsButton.setEnabled(false);
					addedSearchAlbums=true;

					addPhotosButton.setEnabled(true);
					removePhotosButton.setEnabled(true);
					newAlbumButton.setEnabled(true);
					deleteAlbumButton.setEnabled(true);
					startSlideShowButton.setEnabled(true);
					ratePhotosButton.setEnabled(true);
					flagPhotosButton.setEnabled(true);


				}	
				catch(NullPointerException ex){
					JOptionPane.showMessageDialog(null, "You must choose the all Photos icon to include search based albums.");
					return;
				}

			}});
		panel2.add(addSearchBasedAlbumsButton);
		return panel2;
	}
	private JPanel makeButtonPanel() {
		JPanel panel = new JPanel();

		// Using a BoxLayout so that buttons will be horizontally aligned
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		newAlbumButton = new JButton("New Album");
		newAlbumButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String newAlbumName = promptForAlbumName();
				if (newAlbumName == null){
					return;
				}
				if (newAlbumName.equals("")){
					JOptionPane.showMessageDialog(null, "You must give your album a name. Try again.");
					return;
				}
				//	try{
				Album a=new Album(newAlbumName);
				if ((rootAlbum.nameAlreadyExists(newAlbumName))==false){//if there are no name duplicates
					if(getSelectedTreeNode().getUserObject().toString().equals("All photos")){
						rootAlbum.registerChildAlbum((AlbumInterface) a);
					}
					if (!getSelectedTreeNode().getUserObject().toString().equals("All photos")){
						((Album) rootAlbum.getSubAlbum(getSelectedTreeNode().getUserObject().toString())).registerChildAlbum((AlbumInterface) a);
					}

				}
				else{
					JOptionPane.showMessageDialog(null,"That name is already in use. Please pick another one.");
					return;
				}
				//				}
				//				catch(NullPointerException ex){
				//					JOptionPane.showMessageDialog(null, "You must choose the all Photos icon to add a new album.");
				//					return;
				//				}
				DefaultMutableTreeNode trnode= new DefaultMutableTreeNode();
				trnode.setUserObject(new DefaultMutableTreeNode(newAlbumName));
				DefaultTreeModel model = (DefaultTreeModel) (albumTree.getModel());
				try{
					model.insertNodeInto(trnode,getSelectedTreeNode(), getSelectedTreeNode().getChildCount());
				}
				catch(NullPointerException ex){
					JOptionPane.showMessageDialog(null, "You must choose the all Photos icon to add a new album.");
					return;
				}
				albumTree.scrollPathToVisible(new TreePath(trnode.getPath()));
				System.out.println(rootAlbum.getChildAlbumSize());
				System.out.println("new album " + newAlbumName + " as subalbum of " + getSelectedTreeNode());


			}
		});
		panel.add(newAlbumButton);

		deleteAlbumButton = new JButton("Delete Album");
		deleteAlbumButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//		try{// this checks first to delete sub albums to the sub album,then to delete the album from root.
				if (!getSelectedTreeNode().getUserObject().toString().equals("All photos")){

					//delete sub albums from root album.
				}
				rootAlbum.deleteSubAlbum(getSelectedTreeNode().getUserObject().toString());
				//				}
				//				catch(NullPointerException esx){
				//					JOptionPane.showMessageDialog(null,"You must highlight the album you want to delete");
				//					return;
				//				}
				previewPane.display();//update the view

				DefaultTreeModel model = (DefaultTreeModel) (albumTree.getModel());
				try{
					model.removeNodeFromParent(getSelectedTreeNode());
				}
				catch (IllegalArgumentException k){
					JOptionPane.showMessageDialog(null,"Can't delete root album.");
					return;
				}
				catch (NullPointerException s){
					JOptionPane.showMessageDialog(null,"You did not choose any album");
					return;
				}
				Set<Photo>emptyPhotos=new HashSet<Photo>();
				previewPane.setPhotos(emptyPhotos);//empties the preview pane after that album has been deleted.
				previewPane.display();


			}
		});
		panel.add(deleteAlbumButton);
		addPhotosButton = new JButton("Add Photos");
		addPhotosButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewPane.setPhotos(previewPane.getSelectedPhotos());
				Set<Photo>tempPhotos=new HashSet<Photo>();
				tempPhotos.addAll(previewPane.getSelectedPhotos());//temp photos has the chosen pictures in it.
				Iterator<Photo> iterator = tempPhotos.iterator();
				while(iterator.hasNext()){
					Photo p=iterator.next();
					try{	
						if (rootAlbum.getSubAlbum((getSelectedTreeNode().getUserObject().toString())).
								getParentAlbum().getPhotos().contains(p)){ //you can only add photos to an album
																		   //if they are in the parent album also.
							
							rootAlbum.getSubAlbum((getSelectedTreeNode().getUserObject().toString())).addPhoto(p);
						}
						else{
							JOptionPane.showMessageDialog(null,"You cannot add a photo to a album if\nparent album does not contain the photo.");
							return;
						}
					}
					catch (NullPointerException ex){
						JOptionPane.showMessageDialog(null,"You must add the photo first to the parent album.");
						return;
					}
					}
				
				System.out.println("add " + previewPane.getSelectedPhotos().size() 
						+ " photos to album " + getSelectedTreeNode());
				try{
					previewPane.setPhotos(rootAlbum.getSubAlbum(getSelectedTreeNode().getUserObject().toString()).getPhotos());
					previewPane.display();//update the view
				}
				catch(NullPointerException ex){
					JOptionPane.showMessageDialog(null,"Can't add photos to root album.");
					return;
				}
			}}
				);
		panel.add(addPhotosButton);

		removePhotosButton = new JButton("Remove Photos");
		removePhotosButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewPane.setPhotos(previewPane.getSelectedPhotos());
				Set<Photo>tempPhotos=new HashSet<Photo>();
				tempPhotos.addAll(previewPane.getSelectedPhotos());//temp photos has the chosen pictures in it.
				Iterator<Photo> iterator = tempPhotos.iterator();

				if(!getSelectedTreeNode().getUserObject().toString().equals("All photos")){
					Iterator<? extends AlbumInterface>iterator2=(rootAlbum.getSubAlbum(getSelectedTreeNode().getUserObject().toString()).descendants()).iterator();
					while (iterator.hasNext()){
						Photo p=iterator.next();

						while(iterator2.hasNext()){ //iterates through the albums own child albums
							Album a=(Album) iterator2.next();

							if (a.getPhotos().contains(p)){
								JOptionPane.showMessageDialog(null,"Also deleting the photo from the sub-album(s).");
								a.getPhotos().remove(p);
							}
						}
						((Album) rootAlbum.getSubAlbum(getSelectedTreeNode().getUserObject().toString())).getPhotos().remove(p);//deletes the photos from the correct album
					}
					previewPane.setPhotos(rootAlbum.getSubAlbum(getSelectedTreeNode().getUserObject().toString()).getPhotos());
					previewPane.display();//update the view
				}
				else{
					while (iterator.hasNext()){
						Photo p=iterator.next();

						if (rootAlbum.getPhotos().contains(p)){
							rootAlbum.getPhotos().remove(p);
						}
					}

					previewPane.setPhotos(rootAlbum.getPhotos());
					previewPane.display();//update the view
				}


			}
		});
		panel.add(removePhotosButton);


		startSlideShowButton = new JButton("Start slideshow");
		startSlideShowButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (getSelectedTreeNode().getUserObject().toString().equals("All photos")){
					SlideShowWindow showAllPhotos=new SlideShowWindow((AlbumInterface) rootAlbum);
					showAllPhotos.setVisible(true);
				}
				else{
					AlbumInterface a= rootAlbum.getSubAlbum(getSelectedTreeNode().getUserObject().toString());
					
					if (a.getPhotos().size()>0){
					SlideShowWindow show=new SlideShowWindow(a);
					show.setVisible(true);
					}
					else{
						JOptionPane.showMessageDialog(null, "No photos to be shown.");
					}
				}
			}
		});

		panel.add(startSlideShowButton);

		flagPhotosButton = new JButton("Add/Remove Flag");
		flagPhotosButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Set<Photo>tempPhotos=new HashSet<Photo>();
				tempPhotos.addAll(previewPane.getSelectedPhotos());//temp photos has the pictures you want to flag

				if ((!getSelectedTreeNode().getUserObject().toString().equals("All photos"))
						&&(!getSelectedTreeNode().getUserObject().toString().equals("Flagged"))
						&&(!getSelectedTreeNode().getUserObject().toString().equals("Great Photos"))){ 



					AlbumInterface a= rootAlbum.getSubAlbum(getSelectedTreeNode().getUserObject().toString());
					Set<Photo>photoSet=a.getPhotos();



					Set<Photo>flaggedPhotoSet=new HashSet<Photo>(); //the photo set that contains similar elements to the 
					//chosen pictures and the album in question



					flaggedPhotoSet.addAll(photoSet);
					flaggedPhotoSet.retainAll(tempPhotos);

					//flagged photo set IS the attribute keeping the flagged photos.

					Iterator<Photo> iterator = flaggedPhotoSet.iterator();
					while(iterator.hasNext()) {
						Photo p=iterator.next();

						if (a.getPhotos().contains(p)){
							if (p.getFlagged()==false){
								p.setFlagged(true);
								//to avoid receiving duplicates.
							}
							else{
								p.setFlagged(false);
							}
						}
					}
				}
				else{
					Set<Photo>photoSet=rootAlbum.getPhotos();

					Set<Photo>flaggedPhotoSet=new HashSet<Photo>();


					flaggedPhotoSet.addAll(photoSet);
					flaggedPhotoSet.retainAll(tempPhotos);

					Iterator<Photo> iterator = flaggedPhotoSet.iterator();
					while(iterator.hasNext()) {//iterates through 
						Photo p =iterator.next();

						if (rootAlbum.getPhotos().contains(p)){
							if (p.getFlagged()==false){
								p.setFlagged(true);
							}
							else{
								p.setFlagged(false);
							}
						}
					}
				}



				previewPane.display();//updating the view
			}
		});
		panel.add(flagPhotosButton);


		ratePhotosButton = new JButton("Rate");
		ratePhotosButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Set<Photo>tempPhotos=previewPane.getSelectedPhotos(); //the photos you want to rate.


				JFrame frame5=new JFrame("Rate");
				JPanel panel5=new JPanel();
				setLayout(new GridBagLayout());		
				GridBagConstraints gc = new GridBagConstraints();

				JRadioButton rating1=new JRadioButton("1    ");
				JRadioButton rating2=new JRadioButton("2    ");
				JRadioButton rating3=new JRadioButton("3    ");
				JRadioButton rating4=new JRadioButton("4    ");
				JRadioButton rating5=new JRadioButton("5    ");


				Box sizeBox = Box.createHorizontalBox();
				ButtonGroup ratingGroup=new ButtonGroup();
				ratingGroup.add(rating1);
				ratingGroup.add(rating2);
				ratingGroup.add(rating3);
				ratingGroup.add(rating4);
				ratingGroup.add(rating5);
				sizeBox.add(rating1);
				sizeBox.add(rating2);
				sizeBox.add(rating3);
				sizeBox.add(rating4);
				sizeBox.add(rating5);

				gc.gridx = 0; //radiobuttons.
				gc.gridy = 0;
				gc.gridwidth=2;
				panel5.add(sizeBox,gc);

				JButton confirm=new JButton("Confirm");
				gc.gridwidth=2;
				gc.gridx=1;
				gc.gridy=2;

				confirm.addActionListener(new ActionListener(){//What happens when we click confirm.
					@Override
					public void actionPerformed(ActionEvent arg0) {
						int rating = 0;
						if (rating1.isSelected()){
							rating=1;
						}
						else if(rating2.isSelected()){
							rating=2;
						}
						else if (rating3.isSelected()){
							rating=3;
						}
						else if (ratingGroup.getSelection()==null){
							return;
						}
						else if(rating4.isSelected()){
							rating=4;
						}
						else if (rating5.isSelected()){
							rating=5;
						}
						Set<Photo>photoSet=rootAlbum.getPhotos();

						Set<Photo>ratedPhotoSet=new HashSet<Photo>();
						ratedPhotoSet.addAll(photoSet);
						ratedPhotoSet.retainAll(tempPhotos); //now the set has all the photos to be rated.

						Iterator<Photo> iterator = ratedPhotoSet.iterator();
						while(iterator.hasNext()) {//iterates through 
							Photo p =iterator.next();
							if (rootAlbum.getPhotos().contains(p)){
								if (rating==1){
									p.setRating(1);
								}
								else if (rating==2){
									p.setRating(2);
								}
								else if (rating==3){
									p.setRating(3);
								}

								//GREAT PHOTOS
								else if (rating==4){
									p.setRating(4);
								}
								else if (rating==5){
									p.setRating(5);
								}
							}
						}
						frame5.setVisible(false); 
						frame5.dispose(); //Destroy the JFrame object
						previewPane.display();//updating the view
					}
				});
				panel5.add(confirm,gc);
				add(panel5,gc);

				panel5.setLocation((frame5.getWidth()-panel.getWidth())/2, 0); // 0 is just the Y location
				frame5.add(panel5);
				frame5.setSize(230,120);
				frame5.setVisible(true);
				frame5.setLocationRelativeTo(null);
			}
		});
		panel.add(ratePhotosButton);

		addPhotosButton.setEnabled(false);
		removePhotosButton.setEnabled(false);
		newAlbumButton.setEnabled(false);
		deleteAlbumButton.setEnabled(false);
		startSlideShowButton.setEnabled(false);
		ratePhotosButton.setEnabled(false);
		flagPhotosButton.setEnabled(false);

		return panel;
	}

	/**
	 * Make the tree showing album names.
	 */
	private JTree makeCatalogTree() {
		DefaultMutableTreeNode tree_root = new DefaultMutableTreeNode("all photos");
		tree_root.setUserObject(root);

		final JTree tree = new JTree(tree_root);
		tree.setMinimumSize(new Dimension(200,400));

		tree.setToggleClickCount(3); // so that we can use double-clicks for previewing instead of expanding/collapsing

		DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
		selectionModel.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setSelectionModel(selectionModel);

		tree.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { //changed this so that it identifies if it is all photos-folder or ANY OTHER FOLDER.
				// if left-double-click @@@changed =2 to ==1
				try{
					if (getSelectedTreeNode().getUserObject().toString().equals("Flagged") //disable some features
							||getSelectedTreeNode().getUserObject().toString().equals("Great Photos")){

						addPhotosButton.setEnabled(false);
						removePhotosButton.setEnabled(false);
						newAlbumButton.setEnabled(false);
						deleteAlbumButton.setEnabled(false);
					}
					else if (addedSearchAlbums==true){
						addPhotosButton.setEnabled(true);
						removePhotosButton.setEnabled(true);
						newAlbumButton.setEnabled(true);
						deleteAlbumButton.setEnabled(true);
					}
					if (e.getButton() == MouseEvent.BUTTON1
							&& e.getClickCount() == 2
							&&(!(getSelectedTreeNode().getUserObject().toString().equals("All photos")))
							&&(!(getSelectedTreeNode().getUserObject().toString().equals("Flagged")))
							&&(!(getSelectedTreeNode().getUserObject().toString().equals("Great Photos")))) {
						previewPane.setPhotos(rootAlbum.getSubAlbum(getSelectedTreeNode().getUserObject().toString()).getPhotos());
						previewPane.display();
						System.out.println("show the photos for album " + getSelectedTreeNode());
					}
					
					else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2&&(getSelectedTreeNode().getUserObject().toString().equals("All photos"))){
						previewPane.setPhotos(rootAlbum.getPhotos());
						previewPane.display();
					}

					
					
					//now fetch the pre-calculated search result from the albums, as specified.
					else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 
							&& (getSelectedTreeNode().getUserObject().toString().equals("Flagged"))){
						Set<Photo>flaggedPhotos=flaggedAlbum.getPhotos(); //catches the pre calculated flagged photos from observer
						previewPane.setPhotos(flaggedPhotos);
						previewPane.display();

					}

					else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 
							&& (getSelectedTreeNode().getUserObject().toString().equals("Great Photos"))){
						Set<Photo>greatPhotos=greatAlbum.getPhotos(); //catches the pre calculate great photos from observer
						previewPane.setPhotos(greatPhotos);
						previewPane.display();


					}





				}
				catch (NullPointerException ex){

				}
			}});	

		return tree;
	}

	/**
	 * Return the album currently selected in the album tree.
	 * Returns null if no selection.
	 */
	private DefaultMutableTreeNode getSelectedTreeNode() {
		return (DefaultMutableTreeNode) albumTree.getLastSelectedPathComponent();
	}


	/**
	 * Pop up a dialog box prompting the user for a name for a new album.
	 * Returns the name, or null if the user pressed Cancel.
	 */
	private String promptForAlbumName() {
		return (String)
				JOptionPane.showInputDialog(
						albumTree, 
						"Album Name: ", 
						"Add Album",
						JOptionPane.PLAIN_MESSAGE, 
						null, 
						null, 
						"");		
	}
	private Set<Photo>getPhotos(){
		return photos;
	}
}
