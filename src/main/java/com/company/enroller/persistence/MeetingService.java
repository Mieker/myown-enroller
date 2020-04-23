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
		Collection<Participant> listOfparticipants = meeting.getParticipants();
		String login;
		for (Participant p : listOfparticipants) {
			login = p.getLogin();
			if (givenlogin.equals(login)) {
				return true;
			}
		}
		return false;
	}

	public void updateMeeting(Meeting oldOne, Meeting newOne) {
		// TODO
		Transaction transaction = this.session.beginTransaction();
		oldOne.setTitle(newOne.getTitle());
		oldOne.setDescription(newOne.getDescription());
		oldOne.setDate(newOne.getDate());
		session.update(oldOne);
		transaction.commit();
	}

	public void deleteParticipant(Meeting meeting, String login) {
		Collection<Participant> listOfParticipants = meeting.getParticipants();
		for (Participant p : listOfParticipants) {
			if (p.getLogin().equals(login)) {
				listOfParticipants.remove(p);
			}
		}
	}
}