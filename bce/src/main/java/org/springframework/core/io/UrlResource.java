package org.springframework.core.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.acgist.bce.Encrypt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UrlResource extends AbstractFileResolvingResource {

    @Nullable
    private final URI uri;
    private final URL url;
    @Nullable
    private volatile URL cleanedUrl;

    public UrlResource(URI uri) throws MalformedURLException {
        this.uri = uri;
        this.url = uri.toURL();
    }

    public UrlResource(URL url) {
        this.uri = null;
        this.url = url;
    }

    public UrlResource(String path) throws MalformedURLException {
        this.uri = null;
        this.url = new URL(path);
        this.cleanedUrl = getCleanedUrl(this.url, path);
    }

    public UrlResource(String protocol, String location) throws MalformedURLException {
        this(protocol, location, null);
    }

    public UrlResource(String protocol, String location, @Nullable String fragment) throws MalformedURLException {
        try {
            this.uri = new URI(protocol, location, fragment);
            this.url = this.uri.toURL();
        } catch (URISyntaxException e) {
            final MalformedURLException ex = new MalformedURLException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }

    private static URL getCleanedUrl(URL originalUrl, String originalPath) {
        final String cleanedPath = StringUtils.cleanPath(originalPath);
        if (!cleanedPath.equals(originalPath)) {
            try {
                return new URL(cleanedPath);
            } catch (MalformedURLException e) {
            }
        }
        return originalUrl;
    }

    private URL getCleanedUrl() {
        URL cleanedUrl = this.cleanedUrl;
        if (cleanedUrl != null) {
            return cleanedUrl;
        }
        cleanedUrl = UrlResource.getCleanedUrl(this.url, (this.uri != null ? this.uri : this.url).toString());
        this.cleanedUrl = cleanedUrl;
        return cleanedUrl;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        final URLConnection connection = this.url.openConnection();
        ResourceUtils.useCachesIfNecessary(connection);
        try {
            if(Encrypt.decrypt) {
                final String name = this.url.toString();
                if (name.contains(Encrypt.FILE_PATH) && name.endsWith(Encrypt.FILE_SUFFIX)) {
                    log.debug("加载文件：{}", name);
                    try (final InputStream input = connection.getInputStream();) {
                        return new ByteArrayInputStream(Encrypt.decrypt(input.readAllBytes()));
                    } catch (Exception e) {
                        log.error("加载文件异常：{}", name);
                    }
                }
            }
            return connection.getInputStream();
        } finally {
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).disconnect();
            }
        }
    }

    @Override
    public URL getURL() {
        return this.url;
    }

    @Override
    public URI getURI() throws IOException {
        if (this.uri != null) {
            return this.uri;
        } else {
            return super.getURI();
        }
    }

    @Override
    public boolean isFile() {
        if (this.uri != null) {
            return super.isFile(this.uri);
        } else {
            return super.isFile();
        }
    }

    @Override
    public File getFile() throws IOException {
        if (this.uri != null) {
            return super.getFile(this.uri);
        } else {
            return super.getFile();
        }
    }

    @Override
    public Resource createRelative(String relativePath) throws MalformedURLException {
        return new UrlResource(this.createRelativeURL(relativePath));
    }

    protected URL createRelativeURL(String relativePath) throws MalformedURLException {
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        relativePath = StringUtils.replace(relativePath, "#", "%23");
        return new URL(this.url, relativePath);
    }

    @Override
    public String getFilename() {
        return StringUtils.getFilename(this.getCleanedUrl().getPath());
    }

    @Override
    public String getDescription() {
        return "URL [" + this.url + "]";
    }

    @Override
    public boolean equals(@Nullable Object target) {
        return (this == target || (target instanceof UrlResource && this.getCleanedUrl().equals(((UrlResource) target).getCleanedUrl())));
    }

    @Override
    public int hashCode() {
        return this.getCleanedUrl().hashCode();
    }

}
