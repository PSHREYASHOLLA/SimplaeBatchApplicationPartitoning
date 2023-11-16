package com.example.postgresql.partitioner; 
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(final Customer item) throws Exception {
       System.out.println(item.getFirstName());
       if("Valerie".equals(item.getFirstName()))
       {
    	   System.out.println("Sumne");
       }
        return item;
    }
}