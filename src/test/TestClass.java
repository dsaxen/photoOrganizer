package test;


//public class TestClass { //Daniel Saxén. 38768. Coverage 100 %.
//
//	@Test
//	public void testAlbumClass(){
//		Album a=new Album("kraka"); //gör en ny album-grupp
//		Album b=new Album("krar");
//		Album c=new Album("krss");
//		Album d=new Album("ksrs"); 
//	
//		
//		File f=new File("sdgk_package\\sample-photos\\applepicking10.jpg");
//		Photo p=new Photo(f);
//		File f2=new File("sdgk_package\\sample-photos\\applepicking12.jpg");
//		Photo p2=new Photo(f2);
//		//skapade två filer för foton
//		assertEquals(p.getFile(),f);
//		
//		
//		assertTrue(a.classInvariant());
//		a.registerChildAlbum(b); //skapar subalbum till albumet "a"
//		b.registerChildAlbum(c);
//		assertEquals(a.getChildAlbumSize(),1); 
//		
//		a.addPhoto(p); 
//		assertEquals(a.getAlbumsPhotoAmount(),1);
//		b.addPhoto(p2);
//		assertEquals(b.getAlbumsPhotoAmount(),1);
//		
//		a.deletePhoto(p);
//		assertEquals(a.getAlbumsPhotoAmount(),0);
//		assertEquals(a.getAlbumName(),"kraka"); 
//		
//		assertTrue(a.allSubAlbums().contains(b)&a.allSubAlbums().contains(c));
//		assertTrue(b.allSuperAlbums().contains(a));
//		assertTrue(c.allSuperAlbums().contains(b));
//		assertTrue(b.isParentAlbum(a));//a är parentalbum till b
//		assertTrue(a.isChildAlbum(b));//b är childalbum till a
//		
//		d.registerParentAlbum(a);//a registreras till parent för d
//		assertTrue(a.isChildAlbum(d));
//		
//		assertEquals(a.getChildAlbumSize(),2); 
//		assertEquals(b.getParentAlbumSize(),1);  
//		assertTrue(b.getParentAlbumse().contains(a)); 
//		
//		assertTrue(a.getChildAlbums().contains(b));
//		assertFalse(a.getChildAlbums().contains(c));
//		a.setAlbumName("tee");
//		assertEquals(a.getAlbumName(),"tee"); 
//	}
//}
