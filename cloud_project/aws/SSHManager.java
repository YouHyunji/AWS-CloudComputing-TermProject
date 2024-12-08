package aws;

/*
 * SSH 접속 관리 클래스
 * JSch 라이브러리를 사용하여 원격 명령 실행
 */

import com.jcraft.jsch.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SSHManager {
    private String host; // 원격 호스트 IP
    private String user; // SSH 사용자
    private String privateKeyPath; // 프라이빗 키 경로

    // 생성자
    public SSHManager(String host, String user, String privateKeyPath) {
        this.host = host;
        this.user = user;
        this.privateKeyPath = privateKeyPath;
    }

    // 명령 실행
    public String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath); // SSH 키 추가

            // 세션 생성 및 설정
            Session session = jsch.getSession(user, host, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // 명령 실행 채널 열기
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            channel.setInputStream(null);
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();

            // 명령 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 채널 및 세션 종료
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            System.out.println("SSH 명령 실행 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return output.toString();
    }
}
