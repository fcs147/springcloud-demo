package com.fz.springcloud.Controller;

import com.fz.springcloud.entities.CommonResult;
import com.fz.springcloud.entities.Payment;
import com.fz.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author:
 * @date: 2021/7/8 21:26
 * @description:
 */
@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;
    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create" )
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("*********插入结果"+result);
        if(result>0){
            return new CommonResult( 200,"成功,serverPort" +serverPort,result);
        }else {
            return new CommonResult( 444,"失败" ,null);
        }

    }
    @GetMapping(value = "/payment/get/{id}" )
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("*********插入结果1"+payment);
        if(payment!=null){
            return new CommonResult( 200,"成功,serverPort" +serverPort ,payment);
        }else {
            return new CommonResult( 444,"失败" ,null);
        }

    }
    @GetMapping(value = "/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices( );
        for (String element : services) {
            log.info("*******element"+element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getInstanceId()+"\t"+instance.getHost()+"\t"+instance.getPort() +"\t"+instance.getUri() );
        }
        return this.discoveryClient;
    }

}