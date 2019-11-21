package org.jboss.resteasy.examples.contacts.persistence;


import junit.framework.Assert;
import org.jboss.resteasy.examples.contacts.core.Contact;
import org.jboss.resteasy.examples.contacts.core.ContactAttrs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collection;

/**
 * @author <a href="mailto:obrand@yahoo.com">Olivier Brand</a>
 * Jun 28, 2008
 */
@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"/test-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Commit
@Transactional
public class TestContact {
   private static final String CONTACT_NAME = "olivier";
   private static final String CONTACT_EMAIL = "olivier@yahoo.com";
   private static final String CONTACT_PHONE = "16506193726";

   private static final String CONTACT_NAME_2 = "angela";
   private static final String CONTACT_EMAIL_2 = "angela@yahoo.com";
   private static final String CONTACT_PHONE_2 = "4312432432432";

   private static final String CONTACT_NAME_3 = "john";
   private static final String CONTACT_EMAIL_3 = "john@yahoo.com";
   private static final String CONTACT_PHONE_3 = "4312432432432";

   private static final String CONTACT_NAME_NEW = "newcontact";
   private static final String CONTACT_NAME_NEW2 = "newcontact2";

   // JSR 250 annotation injecting the contactDao bean. Similar to the Spring
   // @Autowired annotation
   @Resource
   private ContactDao contactDao;

   Contact contact1;
   Contact contact2;
   Contact contact3;

   @Autowired
	EntityManagerFactory emf;

   @Before
   public void insertContacts() {
      contact1 = new Contact();
      contact1.setName(CONTACT_NAME);
      contact1.setEmail(CONTACT_EMAIL);
      contact1.setTelephone(CONTACT_PHONE);

      contact2 = new Contact();
      contact2.setName(CONTACT_NAME_2);
      contact2.setEmail(CONTACT_EMAIL_2);
      contact2.setTelephone(CONTACT_PHONE_2);

      contact3 = new Contact();
      contact3.setName(CONTACT_NAME_3);
      contact3.setEmail(CONTACT_EMAIL_3);
      contact3.setTelephone(CONTACT_PHONE_3);

      contact1.getContactChildren().add(contact2);
      contact2.getContactChildren().add(contact1);
      contact2.getContactChildren().add(contact3);
      contact3.getContactChildren().add(contact2);

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(contact1);
		em.getTransaction().commit();
	}

	@After
   public void deleteContacts() {
      contactDao.deleteContact(contactDao.findContactByName(contact1.getName()));

		Contact contact = contactDao.findContactByName(CONTACT_NAME_NEW);
		if (contact != null) {
			contactDao.deleteContact(contact);
		}
   }

   @Test
   public void simpleTest() {
      Assert.assertNotNull(contactDao);
   }

   @Test
   public void testFindByName() {
      Assert.assertNotNull(contactDao.findContactByName(CONTACT_NAME));
   }

   @Test
   public void testFindById() {
      Assert.assertNotNull(contactDao.findContactById(contact1.getId()));
   }

   @Test
   public void testFindAllContacts() {
      Assert.assertFalse(contactDao.findAllContacts().isEmpty());
      Assert.assertEquals(3, contactDao.findAllContacts().size());
   }

   @Test
   public void testFindByNameContacts() {
      Contact contact = contactDao.findContactByName(CONTACT_NAME_2);
      Assert.assertNotNull(contact);
      Assert.assertEquals(2, contact.getContactChildren().size());
   }

   @Test
   public void testFindByEmail() {
      Assert.assertNotNull(contactDao.findContactByEmail(CONTACT_EMAIL));
   }

   @Test
   public void testFindByPhone() {
      Assert.assertNotNull(contactDao.findContactByPhone(CONTACT_PHONE));
   }

   @Test
   public void testFindByAttribute() {
      Assert.assertNotNull(contactDao.findContactByAttribute(
              ContactAttrs.telephone, CONTACT_PHONE));
      Assert.assertNotNull(contactDao.findContactByAttribute(
              ContactAttrs.email, CONTACT_EMAIL));
      Assert.assertNotNull(contactDao.findContactByAttribute(
              ContactAttrs.name, CONTACT_NAME));
      Assert.assertNotNull(contactDao.findContactByAttribute(ContactAttrs.id,
              contact1.getId()));
   }

   @Test
   public void testInsertContact() {
      Contact newContact = new Contact();
      newContact.setEmail("newcontact@yahoo.com");
      newContact.setName(CONTACT_NAME_NEW);
      newContact.setTelephone("3213123123");
      Contact contact = contactDao.findContactByName(CONTACT_NAME_NEW);
      Assert.assertNull(contact);
      contactDao.mergeContact(newContact);
      contact = contactDao.findContactByName(CONTACT_NAME_NEW);
      Assert.assertNotNull(contact);
   }

   @Test
   public void testDeleteContact() {
      Contact newContact = new Contact();
      newContact.setEmail("newcontact2@yahoo.com");
      newContact.setName(CONTACT_NAME_NEW2);
      newContact.setTelephone("3213123134");
      Contact contact = contactDao.findContactByName(CONTACT_NAME_NEW2);
      Assert.assertNull(contact);
      contactDao.mergeContact(newContact);
      contact = contactDao.findContactByName(CONTACT_NAME_NEW2);
      Assert.assertNotNull(contact);
      contactDao.deleteContact(contact);
      contact = contactDao.findContactByName(CONTACT_NAME_NEW2);
      Assert.assertNull(contact);
   }

   @Test
   public void testGetContactsOfContact() {
      Collection<Contact> contacts = contactDao.findContactsOfContact(contact2.getId());
      Assert.assertEquals(2, contacts.size());
   }

}