package com.chengym.activity.common;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class OwnSSLSocketFacyory extends SSLSocketFactory{
    private final SSLSocketFactory sslsocketfactory;

    public OwnSSLSocketFacyory(SSLSocketFactory sslSocketFactory) {
        this.sslsocketfactory = sslSocketFactory;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return sslsocketfactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return sslsocketfactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose) throws IOException {
        final Socket underlyingSocket = sslsocketfactory.createSocket(socket, host, port, autoClose);
        return overrideProtocol(underlyingSocket);
    }

    @Override
    public Socket createSocket(final String host, final int port) throws IOException {
        final Socket underlyingSocket = sslsocketfactory.createSocket(host, port);
        return overrideProtocol(underlyingSocket);
    }

    @Override
    public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort) throws
            IOException {
        final Socket underlyingSocket = sslsocketfactory.createSocket(host, port, localAddress, localPort);
        return overrideProtocol(underlyingSocket);
    }

    @Override
    public Socket createSocket(final InetAddress host, final int port) throws IOException {
        final Socket underlyingSocket = sslsocketfactory.createSocket(host, port);
        return overrideProtocol(underlyingSocket);
    }

    @Override
    public Socket createSocket(final InetAddress host, final int port, final InetAddress localAddress, final int localPort) throws
            IOException {
        final Socket underlyingSocket = sslsocketfactory.createSocket(host, port, localAddress, localPort);
        return overrideProtocol(underlyingSocket);
    }

    private Socket overrideProtocol(final Socket socket) {
        if (!(socket instanceof SSLSocket)) {
            throw new RuntimeException("An instance of SSLSocket is expected");
        }
        ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1"});
        return socket;
    }
}
