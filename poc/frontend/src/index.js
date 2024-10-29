document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
  
    var calendar = new FullCalendar.Calendar(calendarEl, {
      initialView: 'timeGridWeek',
      headerToolbar: {
        center: 'addEventButton',
        start: 'dayGridDay,timeGridWeek'
      },
      nowIndicator: true,
      slotLabelFormat: {
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
      },
      eventTimeFormat: {
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
      },
      slotMinTime: '08:00:00',
      slotMaxTime: '20:00:00',
      dayHeaderFormat: { 
        weekday: 'short', 
        day: '2-digit', 
        month: '2-digit', 
        year: 'numeric' 
      },
      events: [
        {
          groupId: 'blueEvents', // recurrent events in this group move together
          daysOfWeek: [ '2' ],
          startRecur: "2024-10-06",
          endRecur: "2024-10-10",
          startTime: '10:45:00',
          endTime: '12:45:00'
        },
        {
          daysOfWeek: [ '3' ], // these recurrent events move separately
          startTime: '11:00:00',
          endTime: '11:30:00',
          color: 'red'
        },
        {
          groupId: 'blueEvents', // recurrent events in this group move together
          daysOfWeek: [ '4' ],
          startRecur: "2024-10-10",
          endRecur: "2024-10-15",
          startTime: '10:45:00',
          endTime: '12:45:00'
        }
      ],
      editable : true,
      eventDrop: function(info) {
        console.log(info.event);
      },
      eventClick: function(info) {
        alert('Event: ' + info.event.title);
        alert('Coordinates: ' + info.jsEvent.pageX + ',' + info.jsEvent.pageY);
        alert('View: ' + info.view.type);
    
        // change the border color just for fun
        info.el.style.borderColor = 'red';
      },
      customButtons: {
        addEventButton: {
          text: 'add event...',
          click: function() {
            var dateStr = prompt('Enter a date in YYYY-MM-DD format');
            var date = new Date(dateStr + 'T00:00:00'); // will be in local time
  
            if (!isNaN(date.valueOf())) { // valid?
              calendar.addEvent({
                title: 'dynamic event',
                start: date,
                allDay: true
              });
              alert('Great. Now, update your database...');
            } else {
              alert('Invalid date.');
            }
          }
        }
      },
    });
  
    calendar.render();
  });