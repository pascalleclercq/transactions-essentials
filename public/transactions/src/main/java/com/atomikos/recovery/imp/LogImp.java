package com.atomikos.recovery.imp;

import java.util.Collection;

import com.atomikos.recovery.CoordinatorLogEntry;
import com.atomikos.recovery.CoordinatorLogEntryRepository;
import com.atomikos.recovery.OltpLog;
import com.atomikos.recovery.OltpLogException;
import com.atomikos.recovery.ParticipantLogEntry;
import com.atomikos.recovery.RecoveryException;
import com.atomikos.recovery.RecoveryLog;

public class LogImp implements OltpLog, RecoveryLog {

	private CoordinatorLogEntryRepository repository;

	public void setRepository(CoordinatorLogEntryRepository repository) {
		this.repository = repository;
	}

	@Override
	public void write(CoordinatorLogEntry coordinatorLogEntry)
			throws IllegalStateException {
		if (entryAllowed(coordinatorLogEntry)) {
			repository.put(coordinatorLogEntry.coordinatorId,
					coordinatorLogEntry);
		} else {
			throw new IllegalStateException();
		}

	}

	private boolean entryAllowed(CoordinatorLogEntry coordinatorLogEntry) {
		CoordinatorLogEntry existing = repository
				.get(coordinatorLogEntry.coordinatorId);
		return coordinatorLogEntry.transitionAllowedFrom(existing);
	}

	@Override
	public void remove(String coordinatorId) throws OltpLogException {
		repository.remove(coordinatorId);
	}

	@Override
	public void terminated(ParticipantLogEntry entry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminatedWithHeuristicRollback(ParticipantLogEntry entry) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<ParticipantLogEntry> getCommittingParticipants()
			throws RecoveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void presumedAborting(ParticipantLogEntry entry)
			throws IllegalStateException {
		CoordinatorLogEntry coordinatorLogEntry = repository.get(entry.coordinatorId);
		if (coordinatorLogEntry == null) {
			ParticipantLogEntry[] participantDetails = new ParticipantLogEntry[1];
			participantDetails[0] = entry;
			coordinatorLogEntry = new CoordinatorLogEntry(entry.coordinatorId, participantDetails);
			repository.put(entry.coordinatorId, coordinatorLogEntry);
			throw new IllegalStateException();
		} else {
			CoordinatorLogEntry updated = coordinatorLogEntry.presumedAborting(entry);
			repository.put(updated.coordinatorId, updated);
		}
		
			
	}

	@Override
	public void terminatedWithHeuristicCommit(ParticipantLogEntry entry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminatedWithHeuristicHazard(ParticipantLogEntry entry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminatedWithHeuristicMixed(ParticipantLogEntry entry) {
		// TODO Auto-generated method stub

	}

}
