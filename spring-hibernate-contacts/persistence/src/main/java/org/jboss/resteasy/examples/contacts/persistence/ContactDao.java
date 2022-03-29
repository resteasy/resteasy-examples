package org.jboss.resteasy.examples.contacts.persistence;

import org.jboss.resteasy.examples.contacts.core.Contact;
import org.jboss.resteasy.examples.contacts.core.ContactAttrs;

import java.util.Collection;


/**
 * @author <a href="mailto:obrand@yahoo.com">Olivier Brand</a>
 * Jun 28, 2008
 */
public interface ContactDao {
    public Contact findContactByName(String contactName);

    public Contact findContactById(long id);

    public Contact findContactByEmail(String email);

    public Contact findContactByPhone(String phone);

    public Contact findContactByAttribute(ContactAttrs attribute, Object value);

    public Collection<Contact> findAllContacts();

    public void addContact(Contact contact);

    public void mergeContact(Contact contact);

    public void deleteContact(Contact contact);

    public Collection<Contact> findContactsOfContact(long pid);
}
