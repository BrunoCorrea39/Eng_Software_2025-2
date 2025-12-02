/**
 * 
 */
/**
 * 
 */
module escolinha { 
    requires java.desktop; 
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params; 
    
    opens com.escolinha.test to org.junit.platform.commons;
    exports com.escolinha.domain;
    exports com.escolinha.repository;
    exports com.escolinha.service;
    exports com.escolinha.view;
}