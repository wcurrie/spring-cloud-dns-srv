## What

An experimental implementation of spring cloud's LoadBalancerClient that uses DNS SRV records.

## Test

Run a local dnsmasq instance configured with 2 SRV records for web.service.local. One resolving to google.com.
Another to slashdot.org.

    docker run --rm --name dnsmasq -p 53:53/udp -p 8081:8080 -v $PWD/dnsmasq.conf:/etc/dnsmasq.conf quay.io/jpillora/dnsmasq-gui:latest
    
Seems dnsmasq will round robin between these 2 records. Run dig a few times to see:

    dig @localhost web.service.local SRV +short

Run the silly little app containing DnsSrvLoadBalancerClient.     
    
    run Application
    
Test using curl (or your browser).
    
    curl localhost:8080/

## Where Does the spring magic happen?

Spring boot's LoadBalancerAutoConfiguration adds a LoadBalancerInterceptor that will rewrite a GET request to
https://web.service.local to one of the hosts resolved from a DNS lookup (google.com or slashdot.org).
