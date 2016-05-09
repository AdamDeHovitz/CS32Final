bulkAppControllers.controller("eventsCtrl", 
	function($scope, $rootScope, $http, $ionicModal, $state, $timeout) {
	$("#loading").hide();
	$scope.newMyEvents = false;
	$scope.newMyEventsNum = 0; 
	$scope.newJoinedEvents = false;
	$scope.newJoinedEventsNum = 0; 
	$scope.newPendingEvents = false;
	$scope.newPendingEventsNum = 0; 
	$scope.creationData = {};
	$scope.forms = {};
	$scope.positiveIntRegex = "^[1-9][0-9]*$";
	$scope.moneyRegex = "^\\$?[0-9][0-9\\,]*(\\.\\d{1,2})?$|^\\$?[\\.]([\\d][\\d]?)$";

	
	$scope.goPending = function() {
		$timeout(function(){
			$scope.newPendingEvents = false;
			$scope.newPendingEventsNum = 0; 
		}, 1000);
		$state.go('tab.pending-events');
	};
	
	$scope.goMyEvents = function() {
		$timeout(function(){
			$scope.newMyEvents = false;
			$scope.newMyEventsNum = 0; 
		}, 1000);

		$state.go('tab.my-events');
	};
	
	$scope.goJoined = function() {
		$timeout(function(){
			$scope.newJoinedEvents = false;
			$scope.newJoinedEventsNum = 0; 
		}, 1000);
		$state.go('tab.joined-events');
	};

	$scope.createEvent = function() {
		$scope.creationData["owner_id"] = $rootScope.account.id;
		$scope.creationData["tags"] = [];
		function getLoc(callback) {
        if (navigator.geolocation) {
          var startPos;
          var geoOptions = {
             maximumAge:  2 * 1000,
             timeout: 10 * 1000
          }

          var geoSuccess = function(position) {
            startPos = position;
            $rootScope.lat = startPos.coords.latitude;
            $rootScope.lng = startPos.coords.longitude;

            console.log("retrieved location");
            console.log($rootScope.lat);
            console.log($rootScope.lng);
            callback();
          };
          var geoError = function(error) {
            console.log('Error occurred. Error code: ' + error.code);
            // error.code can be:
            //   0: unknown error
            //   1: permission denied
            //   2: position unavailable (error response from location provider)
            //   3: timed out
            callback();
          };

          navigator.geolocation.getCurrentPosition(geoSuccess, geoError, geoOptions);

        }
        else {
            console.log('Geolocation is not supported for this Browser/OS version yet.');
            callback();
        }
        }


        function createEvent() {
            console.log("STUFF");
            $scope.creationData["lat"] = $rootScope.lat;
            $scope.creationData["lng"] = $rootScope.lng;
            console.log($scope.creationData);


		$.post("/event-create", $scope.creationData, function(responseJSON) {
			console.log(responseJSON);
			responseObject = JSON.parse(responseJSON);
			console.log(responseObject);
		});
		var curData = $scope.creationData;
		$scope.closeModal();
		$state.go('tab.my-events');
		}
		getLoc(createEvent);

		//TODO: need to reset data?

	}
	
	$ionicModal.fromTemplateUrl('new-event-modal.html', {
	    scope: $scope,
	    animation: 'slide-in-up'
	  }).then(function(modal) {
	    $scope.modal = modal;
	  });
	  $scope.openModal = function() {
	    $scope.modal.show();
	  };
	  $scope.closeModal = function() {
	    $scope.modal.hide();
	    $scope.creationData = {};
	  	$scope.forms.eventCreate.$setPristine();
	  	$scope.forms.eventCreate.$setUntouched();
	  };
	  //Cleanup the modal when we're done with it!
	  $scope.$on('$destroy', function() {
	    $scope.modal.remove();
	  });
	  // Execute action on hide modal
	  $scope.$on('modal.hidden', function() {
	    // Execute action
	  });
	  // Execute action on remove modal
	  $scope.$on('modal.removed', function() {
	    // Execute action
	  });

	  $scope.getDistance = function(event) {
              var R = 6378137; // Earth’s mean radius in meter
              var dLat = rad(event.lat - $rootScope.lat);
              var dLong = rad(event.lng - $rootScope.lng);
              var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                                           Math.cos(rad($rootScope.lat)) * Math.cos(rad(event.lat)) *
                                           Math.sin(dLong / 2) * Math.sin(dLong / 2);
                                           var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                                           var d = R * c;
                                          return d; // returns the distance in meter
                                           };
	/*
		if (!$rootScope.userId) {
			$location.path("/");
		}
		var eventId = $routeParams.eventId;
		var postData = $.param({
			json: JSON.stringify({
				userId: userId,
				eventId: eventId
			})
		});
		$http.post("/eventData", postData).success(function(data, status) {
			$scope.events = data.entries;
		});
	*/

});