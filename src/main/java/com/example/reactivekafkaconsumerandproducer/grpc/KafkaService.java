package com.example.reactivekafkaconsumerandproducer.grpc;

import com.example.grpc.kafka.v1.KafkaProto;
import com.example.grpc.kafka.v1.KafkaServiceGrpc;
import com.example.reactivekafkaconsumerandproducer.dto.FakeProducerDTO;
import com.example.reactivekafkaconsumerandproducer.service.ReactiveConsumerService;
import com.example.reactivekafkaconsumerandproducer.service.ReactiveProducerService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GRpcService
public class KafkaService extends KafkaServiceGrpc.KafkaServiceImplBase {

    private final ReactiveProducerService reactiveProducerService;
    private final ReactiveConsumerService reactiveConsumerService;
    private final Logger logger;


    public KafkaService(ReactiveProducerService reactiveProducerService,
            ReactiveConsumerService reactiveConsumerService) {
        this.reactiveProducerService = reactiveProducerService;
        this.reactiveConsumerService = reactiveConsumerService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void produce(Empty request, StreamObserver<KafkaProto.ProduceResponse> responseObserver) {
        FakeProducerDTO fakeProducerDTO = new FakeProducerDTO();
        fakeProducerDTO.setId("test");
        logger.info("Producing message: {}", fakeProducerDTO);
        reactiveProducerService.send(fakeProducerDTO)
                .subscribe(unused -> {
                    logger.info("Message sent haha");
                    responseObserver.onNext(KafkaProto.ProduceResponse.newBuilder().build());
                    responseObserver.onCompleted();
                }, throwable -> {
                    logger.error("Error sending message", throwable);
                    responseObserver.onError(throwable);
                }, () -> {
                    logger.info("Message sent");
                    responseObserver.onNext(KafkaProto.ProduceResponse.newBuilder().build());
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void consume(Empty request, StreamObserver<KafkaProto.ConsumeResponse> responseObserver) {
        reactiveConsumerService.consumeFakeConsumerDTO()
                .subscribe(fakeConsumerDTOS -> {
                    logger.info("Consuming message: {}", fakeConsumerDTOS);
                    KafkaProto.ConsumeResponse.Builder consumeResponseBuilder = KafkaProto.ConsumeResponse.newBuilder();
                    consumeResponseBuilder.setMessage(fakeConsumerDTOS.toString());
                    responseObserver.onNext(consumeResponseBuilder.build());
                    responseObserver.onCompleted();
                }, throwable -> {
                    logger.error("Error consuming message", throwable);
                    responseObserver.onError(throwable);
                });
    }
}
