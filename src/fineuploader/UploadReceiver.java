package fineuploader;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class UploadReceiver extends HttpServlet
{
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        new File("uploads").mkdir();

        String contentLengthHeader = req.getHeader("Content-Length");
        Long expectedFileSize = StringUtils.isBlank(contentLengthHeader) ? null : Long.parseLong(contentLengthHeader);

        try
        {
            resp.setContentType("text/html");
            resp.setStatus(200);

            if (ServletFileUpload.isMultipartContent(req))
            {
                doWriteTempFileForPostRequest(req);
                resp.getWriter().print("{success: true}");
            }
            else
            {
                writeToTempFile(req.getInputStream(), new File("uploads/" + UUID.randomUUID().toString()), expectedFileSize);
                resp.getWriter().print("{success: true}");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            resp.getWriter().print("{success: false}");
        }
    }


    private void doWriteTempFileForPostRequest(HttpServletRequest request) throws Exception
    {
        FileUploadParser form = new FileUploadParser(request, new File("uploads"), getServletContext());

        writeToTempFile(form.getFirstFile().getInputStream(), new File("uploads/" + UUID.randomUUID().toString()), null);
    }

    private File writeToTempFile(InputStream in, File out, Long expectedFileSize) throws IOException
    {
        FileOutputStream fos = null;

        try
        {
            fos = new FileOutputStream(out);

            IOUtils.copy(in, fos);

            if (expectedFileSize != null)
            {
                Long bytesWrittenToDisk = out.length();
                if (!expectedFileSize.equals(bytesWrittenToDisk))
                {
//                    log.warn("Expected file {} to be {} bytes; file on disk is {} bytes", new Object[] { out.getAbsolutePath(), expectedFileSize, 1 });
                    throw new IOException(String.format("Unexpected file size mismatch. Actual bytes %s. Expected bytes %s.", bytesWrittenToDisk, expectedFileSize));
                }
            }

            return out;
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
        finally
        {
            IOUtils.closeQuietly(fos);
        }
    }

}
