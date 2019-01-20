package main.rates;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class RateController {

    @RequestMapping("/getRates")
    public Rate greeting() {
        return new Rate(1, 65, "USD");
    }
}
