package com.vagdedes.spartan.objects.data;

import com.vagdedes.spartan.functionality.important.MultiVersion;
import com.vagdedes.spartan.handlers.stability.TPS;
import com.vagdedes.spartan.utils.java.StringUtils;
import com.vagdedes.spartan.utils.java.math.AlgebraUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cooldowns {

    private final Map<String, Long> hm;

    public Cooldowns(boolean async) {
        hm = (async && !MultiVersion.folia ? new ConcurrentHashMap<>() : new LinkedHashMap<>());
    }

    public void clear() {
        hm.clear();
    }

    public boolean has(String name) {
        return hm.containsKey(name);
    }

    public int get(String name) {
        Long object = hm.get(name);
        return object == null ? 0 :
                Math.max(AlgebraUtils.integerCeil((object - System.currentTimeMillis()) / TPS.tickTimeDecimal), 0);
    }

    public boolean canDo(String name) {
        return get(name) == 0;
    }

    public void add(String name, int amount) {
        if (amount > 0) {
            hm.put(name, System.currentTimeMillis() + (amount * 50L));
        } else {
            hm.remove(name);
        }
    }

    public void add(String[] names, int amount) {
        if (amount > 0) {
            for (String name : names) {
                hm.put(name, System.currentTimeMillis() + (amount * 50L));
            }
        } else {
            for (String name : names) {
                hm.remove(name);
            }
        }
    }

    public void remove(String name) {
        hm.remove(name);
    }

    public void remove(String[] names) {
        for (String name : names) {
            remove(name);
        }
    }

    public void clear(String[] ignore) {
        if (!hm.isEmpty()) {
            List<String> internal = new ArrayList<>();

            for (String name : hm.keySet()) {
                if (!StringUtils.stringContainsPartOfArray(ignore, name)) {
                    internal.add(name);
                }
            }
            remove(internal.toArray(new String[0]));
        }
    }

    public void clear(String s) {
        if (!hm.isEmpty()) {
            List<String> internal = new ArrayList<>();

            for (String name : hm.keySet()) {
                if (name.contains(s)) {
                    internal.add(name);
                }
            }
            remove(internal.toArray(new String[0]));
        }
    }
}
