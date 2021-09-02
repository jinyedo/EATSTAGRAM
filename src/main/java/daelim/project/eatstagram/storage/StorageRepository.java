package daelim.project.eatstagram.storage;

import org.springframework.stereotype.Repository;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.socket.BinaryMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class StorageRepository {

    private final Path rootLocation;

    StorageRepository(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public ByteBuffer save(String filename, BinaryMessage message) {
        ByteBuffer byteBuffer = message.getPayload();
        File file = new File(String.valueOf(rootLocation), filename);
        FileOutputStream out = null;
        FileChannel outChannel = null;

        try {
            byteBuffer.flip(); // byteBuffer 를 읽기 위해서 세팅
            out = new FileOutputStream(file, true); // 생성을 위해 OutputStream 을 연다.
            outChannel = out.getChannel(); // 채널을 열고
            byteBuffer.compact(); // 파일을 복사한다.
            outChannel.write(byteBuffer); // 파일을 쓴다.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (outChannel != null) outChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byteBuffer.position(0); // 파일을 저장하면서 position 값이 변경되었으므로 0으로 초기화한다.
        return byteBuffer;
    }
}
