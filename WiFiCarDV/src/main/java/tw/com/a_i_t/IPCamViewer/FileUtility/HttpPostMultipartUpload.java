package tw.com.a_i_t.IPCamViewer.FileUtility;

/**
 * Created by chrison.feng on 2016/11/22.
 */

import android.util.Log;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

public class HttpPostMultipartUpload extends MultipartEntity {
        private final HttpPostProgressHandler handler;
        public HttpPostMultipartUpload(final HttpPostProgressHandler handler) {
            super();
            this.handler = handler;
        }
        public HttpPostMultipartUpload(final HttpMultipartMode mode, final HttpPostProgressHandler handler) {
            super(mode);

            this.handler = handler;
        }
        public HttpPostMultipartUpload(final HttpMultipartMode mode, final String boundary, final Charset charset, final HttpPostProgressHandler handler) {
            super(mode,boundary,charset);
            this.handler = handler;
        }

        @Override
        public void writeTo(final OutputStream outstream) throws IOException {
            //outstream.
            super.writeTo(new HttpPostOutputStream(outstream, this.handler));
        }

        public static class HttpPostOutputStream extends FilterOutputStream {
            private final HttpPostProgressHandler handler;
            private long transferred;

            public HttpPostOutputStream(final OutputStream out, final HttpPostProgressHandler handler) {

                super(out);
                this.handler = handler;
                this.transferred = 0;
            }

            public void write(byte[] b, int off, int len) throws IOException {
                out.write(b, off, len);
                //Log.d("OOOOO","size=" + len);
                this.transferred += len;
                if( this.handler != null )
                    this.handler.postStatusReport(this.transferred);
            }

            public void write(int b) throws IOException {
                out.write(b);
                this.transferred ++;
                if( this.handler != null )
                    this.handler.postStatusReport(this.transferred);
            }
        }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////








}




//}
