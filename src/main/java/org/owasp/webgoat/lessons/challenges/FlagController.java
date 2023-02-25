/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.challenges;

import lombok.AllArgsConstructor;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.WebSession;
import org.owasp.webgoat.container.users.UserTracker;
import org.owasp.webgoat.container.users.UserTrackerRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FlagController extends AssignmentEndpoint {

  private final UserTrackerRepository userTrackerRepository;
  private final WebSession webSession;
  private final Flags flags;

  @PostMapping(path = "/challenge/flag", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public AttackResult postFlag(@RequestParam String flag) {
    UserTracker userTracker = userTrackerRepository.findByUser(webSession.getUserName());
    Flag expectedFlag = flags.getFlag(webSession.getCurrentLesson());
    final AttackResult attackResult;
    if (expectedFlag.isCorrect(flag)) {
      userTracker.assignmentSolved(
          webSession.getCurrentLesson(), "Assignment" + expectedFlag.number());
      attackResult = success(this).feedback("challenge.flag.correct").build();
    } else {
      userTracker.assignmentFailed(webSession.getCurrentLesson());
      attackResult = failed(this).feedback("challenge.flag.incorrect").build();
    }
    userTrackerRepository.save(userTracker);
    return attackResult;
  }
}
