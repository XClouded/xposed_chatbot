package anet.channel;

import anet.channel.entity.SessionType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class SessionPool {
    private final Map<SessionRequest, List<Session>> connPool = new HashMap();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = this.lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = this.lock.writeLock();

    SessionPool() {
    }

    public void add(SessionRequest sessionRequest, Session session) {
        if (sessionRequest != null && sessionRequest.getHost() != null && session != null) {
            this.writeLock.lock();
            try {
                List list = this.connPool.get(sessionRequest);
                if (list == null) {
                    list = new ArrayList();
                    this.connPool.put(sessionRequest, list);
                }
                if (list.indexOf(session) == -1) {
                    list.add(session);
                    Collections.sort(list);
                    this.writeLock.unlock();
                }
            } finally {
                this.writeLock.unlock();
            }
        }
    }

    public void remove(SessionRequest sessionRequest, Session session) {
        this.writeLock.lock();
        try {
            List list = this.connPool.get(sessionRequest);
            if (list != null) {
                list.remove(session);
                if (list.size() == 0) {
                    this.connPool.remove(sessionRequest);
                }
                this.writeLock.unlock();
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    public List<Session> getSessions(SessionRequest sessionRequest) {
        this.readLock.lock();
        try {
            List list = this.connPool.get(sessionRequest);
            if (list != null) {
                return new ArrayList(list);
            }
            List<Session> list2 = Collections.EMPTY_LIST;
            this.readLock.unlock();
            return list2;
        } finally {
            this.readLock.unlock();
        }
    }

    public Session getSession(SessionRequest sessionRequest) {
        this.readLock.lock();
        try {
            List list = this.connPool.get(sessionRequest);
            Session session = null;
            if (list != null) {
                if (!list.isEmpty()) {
                    Iterator it = list.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            Session session2 = (Session) it.next();
                            if (session2 != null && session2.isAvailable()) {
                                session = session2;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    this.readLock.unlock();
                    return session;
                }
            }
            return null;
        } finally {
            this.readLock.unlock();
        }
    }

    public Session getSession(SessionRequest sessionRequest, int i) {
        this.readLock.lock();
        try {
            List list = this.connPool.get(sessionRequest);
            Session session = null;
            if (list != null) {
                if (!list.isEmpty()) {
                    Iterator it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Session session2 = (Session) it.next();
                        if (session2 != null && session2.isAvailable()) {
                            if (i == SessionType.ALL || session2.mConnType.getType() == i) {
                                session = session2;
                            }
                        }
                    }
                    this.readLock.unlock();
                    return session;
                }
            }
            return null;
        } finally {
            this.readLock.unlock();
        }
    }

    public List<SessionRequest> getInfos() {
        List<SessionRequest> list = Collections.EMPTY_LIST;
        this.readLock.lock();
        try {
            if (this.connPool.isEmpty()) {
                return list;
            }
            ArrayList arrayList = new ArrayList(this.connPool.keySet());
            this.readLock.unlock();
            return arrayList;
        } finally {
            this.readLock.unlock();
        }
    }

    public boolean containsValue(SessionRequest sessionRequest, Session session) {
        this.readLock.lock();
        try {
            List list = this.connPool.get(sessionRequest);
            boolean z = false;
            if (list != null) {
                if (list.indexOf(session) != -1) {
                    z = true;
                }
            }
            return z;
        } finally {
            this.readLock.unlock();
        }
    }
}
