package com.opsit.mystro.models;

import com.gigaspaces.admin.cli.Ping;
import com.gigaspaces.document.DocumentProperties;
import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.metadata.SpaceTypeDescriptor;
import com.gigaspaces.metadata.SpaceTypeDescriptorBuilder;
import com.gigaspaces.metadata.index.SpaceIndexType;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Processor {

    public static void main(String[] args) {

//        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(
//                "classpath:/META-INF/spring/pu.xml");
//
//        System.out.println(context.getBean("gigaSpace"));
//
//        GigaSpace gigaSpace = (GigaSpace) context.getBean("gigaSpace");
//
       UrlSpaceConfigurer configurer = new UrlSpaceConfigurer("jini://*/*/badger");
       GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).create();

        System.out.println("Write (store) a couple of entries in the data grid:");
        gigaSpace.write(new Person(1, "Vincent", "Chase"));
        gigaSpace.write(new Person(2, "Johnny", "Drama"));
        gigaSpace.write(new Person(3, "Vincent1", "Chase"));
        gigaSpace.write(new Person(4, "Johnny1", "Drama"));
        gigaSpace.write(new Person(5, "Johnny2", "Drama"));
        gigaSpace.write(new Person(6, "xyz", "lalla"));
        gigaSpace.write(new Person(7, "xyzdsfdf", "lallasfdsd"));

        System.out.println("Read (retrieve) an entry from the grid by its id:");
        Person result1 = gigaSpace.readById(Person.class, 1);

        System.out.println("Read an entry from the grid using a SQL-like query:");
        Person result2 = gigaSpace.read(new SQLQuery<Person>(Person.class, "firstName=?", "Johnny"));

        System.out.println("Read all entries of type Person from the grid:");
        Person[] results = gigaSpace.readMultiple(new Person());

        System.out.println(results.length);

        registerProductType(gigaSpace);
        gigaSpace.write(createDocumemt());
    }

    public static SpaceDocument createDocumemt() {
        DocumentProperties properties = new DocumentProperties()
                .setProperty("CatalogNumber", "av-9876")
                .setProperty("Category", "Aviation")
                .setProperty("Name", "Jet Propelled Pogo Stick")
                .setProperty("Price", 19.99f)
                .setProperty("Tags",
                        new String[] { "New", "Cool", "Pogo", "Jet" })
                .setProperty("Features",
                        new DocumentProperties()
                                .setProperty("Manufacturer", "Acme")
                                .setProperty("RequiresAssembly", true)
                                .setProperty("NumberOfParts", 42));

        return new SpaceDocument("Product", properties);
    }

    public static void registerProductType(GigaSpace space) {
        // Create type descriptor:
        SpaceTypeDescriptor typeDescriptor = new SpaceTypeDescriptorBuilder(
                "Product").idProperty("CatalogNumber")
                .routingProperty("Category")
                .addPropertyIndex("Name", SpaceIndexType.BASIC)
                .addPropertyIndex("Price", SpaceIndexType.EXTENDED).create();
        // Register type:
        space.getTypeManager().registerTypeDescriptor(typeDescriptor);
    }
}