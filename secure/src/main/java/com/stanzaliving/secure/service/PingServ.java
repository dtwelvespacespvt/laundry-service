package com.stanzaliving.secure.service;//package com.stanzaliving.inception.service;
//
//import com.stanzaliving.inception.helloworld.Greeting;
//import com.stanzaliving.inception.helloworld.Ping;
//import com.stanzaliving.inception.helloworld.PingResponse;
//import com.stanzaliving.inception.helloworld.PingServiceGrpc;
//import io.grpc.stub.StreamObserver;
//import org.lognet.springboot.grpc.GRpcService;
//
//
//@GRpcService
//public class PingServ extends PingServiceGrpc.PingServiceImplBase {
//
// @Override
// public void ping(Ping request, StreamObserver<PingResponse> responseObserver) {
//  String message = "Hello " + request.getStr() + " !";
//  System.out.println(message);
//  PingResponse greeting =
//          PingResponse.newBuilder().setStrRes(message).build();
//  responseObserver.onNext(greeting);
//  System.out.println("test");
//  responseObserver.onCompleted();
// }
//}
