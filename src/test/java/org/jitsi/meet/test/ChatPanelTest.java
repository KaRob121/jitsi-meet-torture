/*
 * Copyright @ 2015 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jitsi.meet.test;

import org.jitsi.meet.test.pageobjects.web.*;
import org.jitsi.meet.test.web.*;
import org.testng.annotations.*;

/*---------NEW CODE----------*/
import org.jitsi.meet.test.util.*;
/*-------END--------*/

import static org.testng.Assert.*;

/**
 * Checks that the chat panel can be opened and closed with a shortcut and
 * with the toolbar button.
 *
 * TODO: Add tests for sending/receiving messages.
 *
 * @author Boris Grozev
 */
public class ChatPanelTest
    extends WebTestBase
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void setupClass()
    {
        super.setupClass();

        ensureOneParticipant();
    }

    /**
     * Opens and closes the chat panel with the toolbar button and with the
     * keyboard shortcut, and checks that the open/closed state is correct.
     */
    @Test
    public void testChatPanel()
    {
        WebParticipant participant = getParticipant1();

        // The chat panel requires a display name to be set.
        participant.setDisplayName("bla");

        ChatPanel chatPanel = participant.getChatPanel();
        Toolbar toolbar = participant.getToolbar();

        assertFalse(
            chatPanel.isOpen(),
            "The chat panel should be initially closed");

        // The chat panel should be open after clicking the button
        toolbar.clickChatButton();
        chatPanel.assertOpen();

        // The chat panel should be closed after pressing the shortcut
        chatPanel.pressShortcut();
        chatPanel.assertClosed();

        // The chat panel should be open after pressing the shortcut
        chatPanel.pressShortcut();
        chatPanel.assertOpen();

        // The chat panel should be closed after clicking the button
        toolbar.clickChatButton();
        chatPanel.assertClosed();

        /*--------------NEW CODE----------*/
        // Open the chat panel for typing.
        toolbar.clickChatButton();
        chatPanel.assertOpen();

        // Send a message to the chat panel.
        String message = chatPanel.sendMessage();

        // Create a new participant.
        ensureTwoParticipants();
        WebParticipant participant2 = getParticipant2();

        // Declare a display name for new participant.
        participant2.setDisplayName("tester2");

        ChatPanel chatPanel2 = participant2.getChatPanel();
        Toolbar toolbar2 = participant2.getToolbar();

        // Open the chat panel.
        toolbar2.clickChatButton();
        chatPanel2.assertOpen();

        // Checks that the message the participant 2 sees is the same as participant 1's original message.
        chatPanel2.checkMessage(message);

        // Participant 2 sends a message with words on the censor list.
        String badMessage = chatPanel2.sendBadMessage();
        // Pause for 3 seconds to check the chat panel.
        TestUtils.waitMillis(3000);
        /*-------END---------*/
    }
}
