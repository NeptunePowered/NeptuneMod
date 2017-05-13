/*
 * This file is part of NeptuneVanilla, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015-2017, Jamie Mansfield <https://github.com/jamierocks>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.neptunepowered.vanilla.launch;

import net.minecraft.launchwrapper.Launch;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NeptuneServerMain {

    private static final String MINECRAFT_SERVER_VERSION = "1.8.9";
    private static final String LAUNCHWRAPPER_VERSION = "1.12";

    private static final String LIBRARIES_DIR = "libraries";

    private static final String MINECRAFT_SERVER_LOCAL = "minecraft_server." + MINECRAFT_SERVER_VERSION + ".jar";
    // TODO: stop using s3
    private static final String MINECRAFT_SERVER_REMOTE =
            "https://s3.amazonaws.com/Minecraft.Download/versions/" + MINECRAFT_SERVER_VERSION + "/" + MINECRAFT_SERVER_LOCAL;

    private static final String LAUNCHWRAPPER_PATH =
            "/net/minecraft/launchwrapper/" + LAUNCHWRAPPER_VERSION + "/launchwrapper-" + LAUNCHWRAPPER_VERSION +".jar";
    private static final String LAUNCHWRAPPER_LOCAL = LIBRARIES_DIR + LAUNCHWRAPPER_PATH;
    private static final String LAUNCHWRAPPER_REMOTE = "https://libraries.minecraft.net" + LAUNCHWRAPPER_PATH;

    private NeptuneServerMain() {
    }

    public static void main(final String[] args) throws Exception {
        // Get the location of our jar
        Path base = Paths.get(NeptuneServerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();

        // Establish whether the Minecraft server jar has already been obtained.
        if (!checkMinecraft(base)) {
            return;
        }

        // Invoke LaunchWrapper
        Launch.main(join(args,
                "--tweakClass", "org.neptunepowered.vanilla.launch.NeptuneServerTweaker"
        ));
    }

    private static String[] join(String[] args, String... prefix) {
        String[] result = new String[prefix.length + args.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(args, 0, result, prefix.length, args.length);
        return result;
    }

    /**
     * Establishes whether the Minecraft server jar has already been obtained, and if not
     * makes an attempt to obtain it.
     *
     * @return {@code True} should the Minecraft server jar be present,
     *         {@code false} if the Minecraft server jar is not present and Neptune failed
     *         to obtain it
     */
    private static boolean checkMinecraft(Path base) throws Exception {
        // Make sure the Minecraft server is available, or download it otherwise
        Path path = base.resolve(MINECRAFT_SERVER_LOCAL);
        if (Files.notExists(path) && !downloadAndVerify(MINECRAFT_SERVER_REMOTE, path)) return false;

        // Make sure LaunchWrapper is available, or download it otherwise
        path = base.resolve(LAUNCHWRAPPER_LOCAL);
        return Files.exists(path) || downloadAndVerify(LAUNCHWRAPPER_REMOTE, path);
    }

    /**
     * Attempts to download a file, from the given remote URL, to the given local Path. Should
     * a download be successful, it will proceed to verify the file.
     *
     * @param remoteUrl The URL to the remote file
     * @param localPath The Path to the local file
     * @return {@code True} should the download (and verification of download) is successful,
     *         {@code false} otherwise
     */
    private static boolean downloadAndVerify(final String remoteUrl, final Path localPath) throws IOException, NoSuchAlgorithmException {
        Files.createDirectories(localPath.getParent());

        final String name = localPath.getFileName().toString();
        final URL url = new URL(remoteUrl);

        System.out.println("Downloading " + name + "... This can take a while.");
        System.out.println(url);
        final URLConnection con = url.openConnection();
        final MessageDigest md5 = MessageDigest.getInstance("MD5");

        try (final ReadableByteChannel source = Channels.newChannel(new DigestInputStream(con.getInputStream(), md5));
                final FileChannel out = FileChannel.open(localPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            out.transferFrom(source, 0, Long.MAX_VALUE);
        }

        final String expected = getETag(con);
        if (!expected.isEmpty()) {
            final String hash = toHexString(md5.digest());
            if (hash.equals(expected)) {
                System.out.println("Successfully downloaded " + name + " and verified checksum!");
            } else {
                Files.delete(localPath);
                throw new IOException("Checksum verification failed: Expected " + expected + ", got " + hash);
            }
        }

        return true;
    }

    private static String getETag(final URLConnection con) {
        final String hash = con.getHeaderField("ETag");
        if (hash == null || hash.isEmpty()) return "";
        if (hash.startsWith("\"") && hash.endsWith("\"")) return hash.substring(1, hash.length() - 1);
        return hash;
    }

    // From http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    private static final char[] hexArray = "0123456789abcdef".toCharArray();

    private static String toHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
