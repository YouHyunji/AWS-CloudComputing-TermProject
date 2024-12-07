package aws;

/*
 * Cloud Computing
 * 2020039100 Yuhyunji
 *
 * Dynamic Resource Management Tool
 * using AWS Java SDK Library
 *
 */

import java.util.Iterator;
import java.util.Scanner;

import javax.swing.plaf.synth.Region;

//import javax.swing.plaf.synth.Region;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;

// AWS EC2 자원 관리를 위한 메인 클래스
public class Main {

    static AmazonEC2 ec2; // AWS EC2 클라이언트 객체

    // AWS EC2 클라이언트를 초기화하는 메서드
    private static void init() throws Exception {
        // 자격 증명 프로파일 로더를 사용하여 AWS 자격 증명 로드
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials(); // 자격 증명 파일에서 읽어옴
        } catch (Exception e) {
            // 자격 증명 로드 실패 시 예외 처리
            throw new AmazonClientException(
                "Cannot load the credentials from the credential profiles file. " +
                "Ensure your credentials file is properly configured.",
                e);
        }
        // AWS 클라이언트를 설정 (기본 리전: us-east-1)
        ec2 = AmazonEC2ClientBuilder.standard()
            .withCredentials(credentialsProvider) // 자격 증명 설정
            .withRegion("us-east-1") // AWS 리전 설정
            .build();
    }

    // 메인 메서드: 메뉴를 표시하고 사용자 입력을 처리
    public static void main(String[] args) throws Exception {

        init(); // AWS 클라이언트 초기화

        Scanner menu = new Scanner(System.in); // 메뉴 입력용 스캐너
        Scanner id_string = new Scanner(System.in); // 인스턴스 ID 입력용 스캐너
        int number = 0; // 사용자 메뉴 선택을 저장할 변수
        
        while (true) {
            // 메인 메뉴 표시
            System.out.println("------------------------------------------------------------");
            System.out.println("           Amazon AWS SDK Control Panel                   ");
            System.out.println("------------------------------------------------------------");
            System.out.println("  1. List instances               2. Available zones       ");
            System.out.println("  3. Start instance               4. Available regions     ");
            System.out.println("  5. Stop instance                6. Create instance       ");
            System.out.println("  7. Reboot instance              8. List images           ");
            System.out.println("                             99. Quit                      ");
            System.out.println("------------------------------------------------------------");
            
            System.out.print("Enter an integer: ");
            
            if (menu.hasNextInt()) {
                number = menu.nextInt(); // 사용자가 입력한 메뉴 번호 저장
            } else {
                System.out.println("Please enter a valid integer.");
                break;
            }

            String instance_id = ""; // 인스턴스 ID를 저장할 변수

            // 사용자의 메뉴 선택에 따라 작업 수행
            switch (number) {
                case 1: 
                    listInstances(); // EC2 인스턴스 목록 출력
                    break;
                case 2: 
                    availableZones(); // 사용 가능한 가용 영역 표시
                    break;
                case 3: 
                    System.out.print("Enter instance id: ");
                    if (id_string.hasNext())
                        instance_id = id_string.nextLine();
                    if (!instance_id.trim().isEmpty()) 
                        startInstance(instance_id); // 특정 EC2 인스턴스 시작
                    break;
                case 4: 
                    availableRegions(); // 사용 가능한 AWS 리전 표시
                    break;
                case 5: 
                    System.out.print("Enter instance id: ");
                    if (id_string.hasNext())
                        instance_id = id_string.nextLine();
                    if (!instance_id.trim().isEmpty()) 
                        stopInstance(instance_id); // 특정 EC2 인스턴스 중지
                    break;
                case 6: 
                    System.out.print("Enter AMI id: ");
                    String ami_id = "";
                    if (id_string.hasNext())
                        ami_id = id_string.nextLine();
                    if (!ami_id.trim().isEmpty()) 
                        createInstance(ami_id); // 새로운 EC2 인스턴스 생성
                    break;
                case 7: 
                    System.out.print("Enter instance id: ");
                    if (id_string.hasNext())
                        instance_id = id_string.nextLine();
                    if (!instance_id.trim().isEmpty()) 
                        rebootInstance(instance_id); // 특정 EC2 인스턴스 재부팅
                    break;
                case 8: 
                    listImages(); // 사용 가능한 AMI 목록 출력
                    break;
                case 99: 
                    System.out.println("Exiting program...");
                    menu.close();
                    id_string.close();
                    return;
                default: 
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // 모든 EC2 인스턴스를 목록으로 출력
    public static void listInstances() {
        System.out.println("Listing instances...");
        boolean done = false; // 반복문 제어 변수
        DescribeInstancesRequest request = new DescribeInstancesRequest(); // 인스턴스 요청 객체
        
        while (!done) {
            DescribeInstancesResult response = ec2.describeInstances(request); // EC2 인스턴스 정보 요청

            for (Reservation reservation : response.getReservations()) {
                for (Instance instance : reservation.getInstances()) {
                    System.out.printf(
                        "[ID] %s, [AMI] %s, [Type] %s, [State] %10s, [Monitoring State] %s\n",
                        instance.getInstanceId(),
                        instance.getImageId(),
                        instance.getInstanceType(),
                        instance.getState().getName(),
                        instance.getMonitoring().getState());
                }
            }
            request.setNextToken(response.getNextToken()); // 다음 페이지의 토큰 설정
            if (response.getNextToken() == null) {
                done = true; // 모든 페이지를 처리했으면 반복 종료
            }
        }
    }

    // 사용 가능한 가용 영역을 표시
    public static void availableZones() {
        System.out.println("Fetching available zones...");
        try {
            DescribeAvailabilityZonesResult result = ec2.describeAvailabilityZones();
            for (AvailabilityZone zone : result.getAvailabilityZones()) {
                System.out.printf("[Zone ID] %s, [Region] %s, [Zone Name] %s\n",
                                  zone.getZoneId(), zone.getRegionName(), zone.getZoneName());
            }
        } catch (AmazonServiceException ase) {
            System.out.println("Error fetching zones: " + ase.getMessage());
        }
    }

    // 특정 EC2 인스턴스를 시작
    public static void startInstance(String instance_id) {
        System.out.printf("Starting instance %s...\n", instance_id);
        StartInstancesRequest request = new StartInstancesRequest()
            .withInstanceIds(instance_id); // 요청에 인스턴스 ID 추가
        ec2.startInstances(request); // 인스턴스 시작
        System.out.printf("Successfully started instance %s\n", instance_id);
    }

     // 사용 가능한 AWS 리전을 표시
     public static void availableRegions() {
        try {
            System.out.println("Fetching available regions...");
            DescribeRegionsResult regions_response = ec2.describeRegions(); // 리전 요청
    
            for (Region region : regions_response.getRegions()) { // AWS SDK Region 클래스 사용
                System.out.printf("[Region] %15s, [Endpoint] %s\n", 
                                  region.getRegionName(), 
                                  region.getEndpoint());
            }
        } catch (Exception e) {
            System.err.println("Error fetching regions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 특정 EC2 인스턴스를 중지
    public static void stopInstance(String instance_id) {
        System.out.printf("Stopping instance %s...\n", instance_id);
        StopInstancesRequest request = new StopInstancesRequest()
            .withInstanceIds(instance_id); // 요청에 인스턴스 ID 추가
        ec2.stopInstances(request); // 인스턴스 중지
        System.out.printf("Successfully stopped instance %s\n", instance_id);
    }

    // AMI ID를 사용하여 새 EC2 인스턴스를 생성
    public static void createInstance(String ami_id) {
        System.out.printf("Creating instance with AMI %s...\n", ami_id);
        RunInstancesRequest run_request = new RunInstancesRequest()
            .withImageId(ami_id) // AMI ID 설정
            .withInstanceType(InstanceType.T2Micro) // 인스턴스 타입 설정
            .withMaxCount(1).withMinCount(1); // 생성할 인스턴스 수 설정
        RunInstancesResult run_response = ec2.runInstances(run_request); // 인스턴스 생성 요청
        String reservation_id = run_response.getReservation().getInstances().get(0).getInstanceId();
        System.out.printf("Successfully created instance %s\n", reservation_id);
    }

    // 특정 EC2 인스턴스를 재부팅
public static void rebootInstance(String instance_id) {
    System.out.printf("Checking the status of instance %s...\n", instance_id);

    // 예외처리: 인스턴스가 중지된 상태일 경우 메시지 출력
    // 인스턴스 상태 확인
    DescribeInstancesRequest describeRequest = new DescribeInstancesRequest()
        .withInstanceIds(instance_id);
    DescribeInstancesResult describeResult = ec2.describeInstances(describeRequest);

    String instanceState = describeResult.getReservations().get(0).getInstances().get(0).getState().getName();

    if (instanceState.equalsIgnoreCase("stopped")) {
        System.out.printf("Instance %s is currently stopped. Reboot is not possible.\n", instance_id);
        return;
    }

    // 인스턴스가 실행 중이면 재부팅 요청
    System.out.printf("Rebooting instance %s...\n", instance_id);
    RebootInstancesRequest request = new RebootInstancesRequest()
        .withInstanceIds(instance_id); // 요청에 인스턴스 ID 추가
    ec2.rebootInstances(request); // 인스턴스 재부팅 요청
    System.out.printf("Successfully rebooted instance %s\n", instance_id);
}

    // 사용 가능한 AMI 목록을 출력
    public static void listImages() {
        try {
            System.out.println("Fetching specific images...");
            DescribeImagesRequest request = new DescribeImagesRequest().withOwners("self"); // 현재 계정 소유 AMI만 검색
            
            DescribeImagesResult results = ec2.describeImages(request);

            // 예외처리: 이미지가 없는 경우
            if (results.getImages().isEmpty()) {
                System.out.println("No images found.");
                return;
            }
    
            for (Image image : results.getImages()) {
                System.out.printf("[Image ID] %s, [Name] %s, [Owner] %s\n",
                                  image.getImageId(), image.getName(), image.getOwnerId());
            }
        } catch (Exception e) {
            System.err.println("Error during describeImages: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}

