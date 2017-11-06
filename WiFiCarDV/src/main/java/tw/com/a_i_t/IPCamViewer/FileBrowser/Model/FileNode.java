package tw.com.a_i_t.IPCamViewer.FileBrowser.Model;
import java.lang.Long;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ;

import tw.com.a_i_t.IPCamViewer.FileBrowser.Model.FileBrowserModel.ModelException ;
import tw.com.a_i_t.IPCamViewer.FileBrowser.Model.FileViewerElement.FileElement ;

import android.graphics.Bitmap;
import android.util.Log ;

public class FileNode extends FileBrowserNode {
	
	public enum Format {
		
		mov, avi, mp4, jpeg, all
	}
	
	public String mName ;
	public Format mFormat ;
	public long mSize ;
	public String mAttr ;
	public String mTime ;
	public Bitmap mTmb = null ;
	
	public boolean mSelected ;
	
	FileNode(Node fileNode) throws ModelException {
		
		super(fileNode) ;
		
		mSelected = false ;
	}
	
	public FileNode(String name, Format format, long size, String attr, String time) throws ModelException {
		
		super(null) ;
		
		mName = name ;
		mFormat = format ;
		mSize = size ;
		mAttr = attr ;
		mTime = time ;
		mSelected = false ;
	}


	@Override
	protected void parseNode(Node node) throws ModelException {

		NodeList children = node.getChildNodes() ;
		
		mName = null ;
		mFormat = null ;
		mSize = 0 ;
		mAttr = null ;
		mTime = null ;
		
		String name = null ;
		String format = null ;
		String size = null ;
		String attr = null ;
		String time = null ;
		
		for (int i = 0 ; i < children.getLength() ; i++) {
			Node child = children.item(i) ;

			if (child.getNodeType() != Node.ELEMENT_NODE)
				continue ;

			if (FileElement.name.matchElement(child)) {
				name = child.getTextContent() ;
			} else if (FileElement.format.matchElement(child)) {
				format = child.getTextContent() ;
			} else if (FileElement.size.matchElement(child)) {
				size = child.getTextContent() ;
			} else if (FileElement.attr.matchElement(child)) {
				attr = child.getTextContent() ;
			} else if (FileElement.time.matchElement(child)) {
				time = child.getTextContent() ;
			} else {
				Log.i("FileNode", "Ignoring unknown element: " + node.getNodeName() 
						+ "/"+ child.getNodeName()) ;
			}
		}
		
		if (name == null || format == null || size == null || attr == null || time == null) {
			throw new ModelException() ;
		}
		
		mName = name ;
		mFormat = FileBrowserModel.strToEnum(Format.class, format) ;
		mSize = Long.valueOf(size) ;
		mAttr = attr ;
		mTime = time ;
		
	}
}
