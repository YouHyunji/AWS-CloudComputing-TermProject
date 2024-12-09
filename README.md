# AWS 동적 자원 관리 프로그램
![image](https://github.com/user-attachments/assets/f5c132a2-542c-41cf-9b93-563270064a37)
<br />

## **📝 프로젝트 개요**
이 프로젝트는 AWS Java SDK를 활용하여 AWS 자원을 동적으로 관리하고 제어할 수 있는 콘솔 애플리케이션이다. <br />
EC2 인스턴스의 생성, 시작, 중지, 상태 확인, 태그 추가 등 주요 관리 기능을 제공하며,<br />
명령어 기반 메뉴 인터페이스를 통해 사용자가 AWS 리소스를 직관적으로 제어할 수 있다. <br />
또한, 인스턴스 ID와 태그를 기반으로 SSH 접속을 지원하여 빠르고 효율적인 인스턴스 관리를 돕는다. 

<br />

<br />

## 👉 메뉴 소개

**1. List instance:** EC2 인스턴스 조회 <br />
**2. Available zones:** 현재 Region의 가용 영역 목록 조회 <br />
**3. Start instance:** 선택한 인스턴스 시작<br />
**4. Available regions:** Region 목록 확인<br />
**5. Stop instance:** 선택한 인스턴스 중지<br />
**6. Create instance:** AMI로 새로운 EC2 인스턴스 생성<br />
**7. Reboot instance:** 선택한 인스턴스 재부팅<br />
**8. List images:** 현재 계정의 AMI 목록 조회<br />
**9. Add tag to instance:** 선택한 인스턴스에 태그 추가<br />
**10. SSH Connection(ID/Tag):** 인스턴스 ID, Tag로 SSH 접속<br />
<br />

## 👉 주요 기능

- **EC2 인스턴스 관리:**  <br />
**인스턴스 조회**: 모든 EC2 인스턴스를 출력 <br />
**인스턴스 상태 관리**: 인스턴스 시작, 중지, 재부팅
**인스턴스 생성**: AMI을 선택하여 새로운 인스턴스 생성
**가용 영역(Zone) 및 Region 조회**: AWS 가용 영역,Region 확인

- **EC2 인스턴스 태그 추가:** <br />
EC2 인스턴스 ID를 입력하여 태그(Key-Value) 추가<br />
1. List instance 메뉴를 통해 각 인스턴스의 태그 확인 가능

- **EC2 인스턴스 ID,TAG로 SSH 접속** <br />
인스턴스 ID 또는 태그(Key-Value)를 입력하여 SSH 접속 <br />
접속 성공 시 SSH 터미널 세션 자동 연결 <br />

<br />
