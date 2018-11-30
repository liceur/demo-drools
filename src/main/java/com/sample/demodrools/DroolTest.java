package com.sample.demodrools;


import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.io.FileInputStream;
import java.math.BigDecimal;

public class DroolTest {


    public static final void main(String[] args) {
        try {

            // load up the knowledge base
            KieBase kbase = readKieBase();
            KieSession ksession = kbase.newKieSession();

            ItemCity item1 = new ItemCity();
            item1.setPurchaseCity(ItemCity.City.PUNE);
            item1.setTypeofItem(ItemCity.Type.MEDICINES);
            item1.setSellPrice(new BigDecimal(10));
            ksession.insert(item1);

            ItemCity item2 = new ItemCity();
            item2.setPurchaseCity(ItemCity.City.PUNE);
            item2.setTypeofItem(ItemCity.Type.GROCERIES);
            item2.setSellPrice(new BigDecimal(10));
            ksession.insert(item2);

            ItemCity item3 = new ItemCity();
            item3.setPurchaseCity(ItemCity.City.NAGPUR);
            item3.setTypeofItem(ItemCity.Type.MEDICINES);
            item3.setSellPrice(new BigDecimal(10));
            ksession.insert(item3);

            ItemCity item4 = new ItemCity();
            item4.setPurchaseCity(ItemCity.City.NAGPUR);
            item4.setTypeofItem(ItemCity.Type.GROCERIES);
            item4.setSellPrice(new BigDecimal(10));
            ksession.insert(item4);

            ksession.fireAllRules();

            System.out.println(item1.getPurchaseCity().toString() + " "
                    + item1.getLocalTax().intValue());

            System.out.println(item2.getPurchaseCity().toString() + " "
                    + item2.getLocalTax().intValue());

            System.out.println(item3.getPurchaseCity().toString() + " "
                    + item3.getLocalTax().intValue());

            System.out.println(item4.getPurchaseCity().toString() + " "
                    + item4.getLocalTax().intValue());

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

//    private static KnowledgeBase readKnowledgeBase() throws Exception {
//
//        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
//
//        kbuilder.add(ResourceFactory.newClassPathResource("Pune.drl"), ResourceType.DRL);
//        kbuilder.add(ResourceFactory.newClassPathResource("Nagpur.drl"), ResourceType.DRL);
//
//        KnowledgeBuilderErrors errors = kbuilder.getErrors();
//
//        if (errors.size() > 0) {
//            for (KnowledgeBuilderError error : errors) {
//                System.err.println(error);
//            }
//            throw new IllegalArgumentException("Could not parse knowledge.");
//        }
//
//        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
//        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
//
//        return kbase;
//    }


    private static KieBase readKieBase() {


        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();


        kfs.write(ResourceFactory.newClassPathResource("Pune.drl"));
        kfs.write(ResourceFactory.newClassPathResource("Nagpur.drl"));


        KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();

        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }
        KieContainer kieContainer =
                ks.newKieContainer(ks.getRepository().getDefaultReleaseId());

        KieBaseConfiguration config = ks.newKieBaseConfiguration();
        config.setOption(EventProcessingOption.STREAM);
        KieBase kieBase = kieContainer.newKieBase(config);


        return kieBase;
    }
}

