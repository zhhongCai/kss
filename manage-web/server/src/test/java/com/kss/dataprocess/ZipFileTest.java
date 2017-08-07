package com.kss.dataprocess;

import com.alibaba.dubbo.common.io.Bytes;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ImmutableList;
import com.kss.dataprocess.web.file.FileController;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by caijiacheng on 18/06/2017.
 */
public class ZipFileTest {


    Logger LOG = LoggerFactory.getLogger(ZipFileTest.class);

    @Test
    public void testZipAndUnzip() throws IOException {
        List<String> files = ImmutableList.of("中文.jpg",
                "084008tpaeqpeh0z5tkzts.jpg",
                "BingWallpaper-2016-12-09.jpg", "1.pdf");;


        List<Path> tmpfiles = Lists.newArrayList();

        Path tmpdir = Files.createTempDirectory("fin-web-file");
        Path toZip = Files.createTempFile(tmpdir, "fin", ".zip");

        for (String name : files) {
            try (InputStream ins = ClassLoader.getSystemResourceAsStream(name)){
                Path tmp_file = Paths.get(tmpdir.toString(), name);
                Files.copy(ins, tmp_file);
                tmpfiles.add(tmp_file);
            }
        }


        FileController.toZip(toZip, tmpfiles);



        LOG.info("toZip => {}", toZip.toString());
        Path todir = Files.createTempDirectory("fin-web-file");

        Collection<Path> unzips = unZip(toZip, todir);

        LOG.info("unzip => {}", todir.toString());

        for (Path p : unzips) {
            boolean hasfind = false;
            for (Path r : tmpfiles) {
                if (p.getFileName().equals(r.getFileName())) {
                    hasfind = true;
                    Assertions.assertThat(Bytes.getMD5(p.toFile())).isEqualTo(Bytes.getMD5(r.toFile()));
                }
            }
            Assertions.assertThat(hasfind);

        }


    }

    static public Collection<Path> unZip(Path from, Path todir) throws IOException {
        List<Path> allFiles = Lists.newArrayList();
        try (FileInputStream fis = new FileInputStream(from.toFile())) {
            try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    Path tmp_file = Paths.get(todir.toString(), entry.getName());
                    Files.copy(zis, tmp_file);
                    allFiles.add(tmp_file);
                }
            }
        }
        return allFiles;
    }


//    @Test
//    public void testStream()
//    {
//
//        List<String> aaa = ImmutableList.of("1", "2", "3", "9", "0", "3");
//
//        aaa.parallelStream().forEach(item -> {
//            LOG.info("Start... " + item);
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            LOG.info("end... " + item);
//        });
//    }


}
