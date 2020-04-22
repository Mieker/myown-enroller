package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = session.createQuery(hql);
		return query.list();
	}

	public Meeting findById(long id) {
		return (Meeting) session.get(Meeting.class, id);
	}

	public Meeting add(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
	}

	public void delete(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
	}

	public void enlistParticipant(Participant participant, long id) {
		Transaction transaction = this.session.beginTransaction();
		Meeting meeting = (Meeting) session.get(Meeting.class, id);
		meeting.addParticipant(participant);
		transaction.commit();
	}

	public Participant findParticipantByLogin(String login) {
		return (Participant) session.get(Participant.class, login);
	}

	public boolean containParticipant(String givenlogin, Meeting meeting) {
		Collection<?> listOfparticipants = meeting.getParticipants();
		String login;
		for (Object p : listOfparticipants) {
			Participant participant = (Participant) p;
			login = participant.getLogin();
			if (givenlogin.equals(login)) {
				return true;
			}
		}
		return false;
	}
}