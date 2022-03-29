package org.jboss.resteasy.examples.contacts.persistence.hibernate;

import org.jboss.resteasy.examples.contacts.core.Contact;
import org.jboss.resteasy.examples.contacts.core.ContactAttrs;
import org.jboss.resteasy.examples.contacts.persistence.ContactDao;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author <a href="mailto:obrand@yahoo.com">Olivier Brand</a> Jun 28, 2008
 */
@SuppressWarnings("unchecked")
public class ContactDaoImpl extends HibernateDaoSupport implements ContactDao {

    public ContactDaoImpl() {
        // TODO Auto-generated constructor stub
    }

    public void mergeContact(Contact contact) {
        getHibernateTemplate().merge(contact);
    }

    public void addContact(Contact contact) {
        getHibernateTemplate().persist(contact);
    }

    public void deleteContact(Contact contact) {
        getHibernateTemplate().delete(contact);
    }

    public Collection<Contact> findAllContacts() {
        return (Collection<Contact>) getHibernateTemplate().find("from Contact c");
        // return getHibernateTemplate().loadAll(Contact.class);
    }

    public Contact findContactByName(final String contactName) {
        return findSingle("FROM Contact c WHERE c.name =:name", "name",
                contactName);
    }

    public Contact findContactById(final long id) {
        return (Contact) getHibernateTemplate().get(Contact.class, id);
    }

    public Contact findContactByAttribute(final ContactAttrs attribute,
                                          final Object value) {
        return findSingle("FROM Contact c WHERE c." + attribute + " =:"
                + attribute, attribute.toString(), value);
    }

    public Contact findContactByEmail(final String email) {
        return findSingle("FROM Contact c WHERE c.email =:email", "email",
                email);
    }

    public Contact findContactByPhone(final String phone) {
        return findSingle("FROM Contact c WHERE c.telephone=:phone", "phone",
                phone);
    }

    private Contact findSingle(String hql, String paramName, Object value) {
        List<Contact> results = (List<Contact>) getHibernateTemplate().findByNamedParam(hql,
                paramName, value);
        return CollectionUtils.hasUniqueObject(results) ? results.get(0) : null;
    }

    public Collection<Contact> findContactsOfContact(long pid) {
        Contact parentContact = findSingle(
                "from Contact c where c.id=:pid", "pid", pid);
        return parentContact.getContactChildren();
    }
}
