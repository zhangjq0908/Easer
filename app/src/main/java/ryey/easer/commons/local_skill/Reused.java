/*
 * Copyright (c) 2016 - 2021 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.commons.local_skill;

import ryey.easer.commons.ImproperImplementationError;

/**
 * An interface to facilitate sharing skill components among different skills.
 * Using this interface is normally unnecessary and discouraged for the moment.
 * Normally, a skill should define its own components (data, data factory, view), with possibly shared intermediate/parent classes.
 * Some historic skills may share some elements, and thus not working properly (see issue #401).
 * This interface facilitate the skill-lookup process in {@link ryey.easer.skills.LocalSkillRegistry}, esp. {@link ryey.easer.skills.LocalSkillRegistry.SkillLookupper}.
 * Currently used only by {@link ryey.easer.skills.operation.intent}.
 * May change its definition in the future.
 */
public interface Reused {
    public String skillID() throws ImproperImplementationError;

    public void setSkillID(String skillID);
}