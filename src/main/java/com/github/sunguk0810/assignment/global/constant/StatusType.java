package com.github.sunguk0810.assignment.global.constant;

/**
 * 작업(Task) 또는 비즈니스 프로세스의 진행 상태를 정의하는 열거형입니다.
 * <p>
 * 배치 작업, 비동기 요청, 데이터 처리 파이프라인 등에서 현재 작업이
 * 어떤 단계(Lifecycle)에 있는지 추적하기 위해 사용됩니다.
 * </p>
 */
public enum StatusType {

    /**
     * 대기 중 (Pending)
     * <p>작업이 생성되었으나, 아직 실행 로직이 시작되지 않은 초기 상태입니다.</p>
     */
    PENDING,

    /**
     * 처리 중 (Processing)
     * <p>작업이 현재 실행 중이거나 데이터가 처리되고 있는 상태입니다.</p>
     */
    PROCESSING,

    /**
     * 완료 (Done)
     * <p>모든 작업이 성공적으로 마무리되어 정상 종료된 상태입니다.</p>
     */
    DONE,

    /**
     * 오류 (Error)
     * <p>작업 처리 도중 예외가 발생하거나, 비즈니스 로직 상 실패하여 중단된 상태입니다.</p>
     */
    ERROR
}