/**
 * JDBM LICENSE v1.00
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "JDBM" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Cees de Groot.  For written permission,
 *    please contact cg@cdegroot.com.
 *
 * 4. Products derived from this Software may not be called "JDBM"
 *    nor may "JDBM" appear in their names without prior written
 *    permission of Cees de Groot.
 *
 * 5. Due credit should be given to the JDBM Project
 *    (http://jdbm.sourceforge.net/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE JDBM PROJECT AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * CEES DE GROOT OR ANY CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2000 (C) Cees de Groot. All Rights Reserved.
 * Contributions are Copyright (C) 2000 by their associated contributors.
 *
 * Adapted for mobile use by Mohammad Hafiz bin Ismail (info@mypapit.net)
 * Copyright 2008 (C) Mohammad Hafiz bin Ismail
 */

package net.mypapit.java;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class MRU  {

private Hashtable hash;
private int _max;
CacheEntry _first,_last;

public MRU (int max) {

 	if (max <= 0) {
		throw new IllegalArgumentException( "MRU cache must contain at least one entry" );
	}
	
	_max = max;
	hash = new Hashtable(10);
	

}

public void put (Object key, Object value) throws CacheEvictionException
{

	CacheEntry entry = (CacheEntry) hash.get(key);
	if (entry!= null) {
		entry.setValue(value);
		touchEntry(entry);
	} else {
		if (hash.size() == _max) {
			entry = purgeEntry();
			entry.setKey(key);
			entry.setValue(value);
			
		} else {
			    entry = new CacheEntry( key, value );

		}
		addEntry(entry);
		hash.put(entry.getKey(),entry);
	}
}

public Object get (Object key)
{
	CacheEntry entry = (CacheEntry) hash.get(key);
	if (entry!=null) {
		touchEntry(entry);
		return entry.getValue();
	} else {
		return null;
	}
}

public void remove (Object key) 
{
	CacheEntry entry = (CacheEntry) hash.get(key);
	if (entry != null) 
	{
		removeEntry(entry);
		hash.remove(entry.getKey());
	}

}

public void removeAll() 
{
	hash = new Hashtable();
	_first = null;
	_last = null;
}

public Enumeration elements() 
{
	return new MRUEnumeration(hash.elements());
}

public void addEntry(CacheEntry entry) 
{
	if (_first == null) {
		_first = entry;
		_last = entry;
	} else {
		_last.setNext(entry);
		entry.setPrevious(_last);
		_last = entry;
	}
}

public void removeEntry(CacheEntry entry) 
{
	  if ( entry == _first )
        {
            _first = entry.getNext();
        }
        if ( _last == entry )
        {
            _last = entry.getPrevious();
        }
        CacheEntry previous = entry.getPrevious();
        CacheEntry next = entry.getNext();
        if ( previous != null )
        {
            previous.setNext( next );
        }
        if ( next != null )
        {
            next.setPrevious( previous );
        }
        entry.setPrevious( null );
        entry.setNext( null );


}

public void touchEntry( CacheEntry entry)
{
	if ( _last == entry )
    {
            return;
    }
    removeEntry( entry );
    addEntry( entry );

}

public CacheEntry purgeEntry() throws CacheEvictionException 
{
		CacheEntry entry = _first;
		removeEntry( entry );
       hash.remove( entry.getKey() );

        entry.setValue( null );
        return entry;


}

}

class CacheEntry
{
    private Object _key;
    private Object _value;

    private CacheEntry _previous;
    private CacheEntry _next;

    CacheEntry( Object key, Object value )
    {
        _key = key;
        _value = value;
    }

    Object getKey()
    {
        return _key;
    }

    void setKey( Object obj )
    {
        _key = obj;
    }

    Object getValue()
    {
        return _value;
    }

    void setValue( Object obj )
    {
        _value = obj;
    }

    CacheEntry getPrevious()
    {
        return _previous;
    }

    void setPrevious( CacheEntry entry )
    {
        _previous = entry;
    }

    CacheEntry getNext()
    {
        return _next;
    }

    void setNext( CacheEntry entry )
    {
        _next = entry;
    }
}

/**
 * Enumeration wrapper to return actual user objects instead of
 * CacheEntries.
 */
class MRUEnumeration implements Enumeration
{
    Enumeration _enum;

    MRUEnumeration( Enumeration enumerate )
    {
        _enum = enumerate;
    }

    public boolean hasMoreElements()
    {
        return _enum.hasMoreElements();
    }

    public Object nextElement()
    {
        CacheEntry entry = (CacheEntry) _enum.nextElement();
        return entry.getValue();
    }
}

class CacheEvictionException extends Exception {



}

