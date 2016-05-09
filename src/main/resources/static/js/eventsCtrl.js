bulkAppControllers
    .controller(
        "eventsCtrl",
        function($scope, $rootScope, $http, $ionicModal, $state, $timeout,
            $ionicPopup) {

	        $scope.newMyEventsNum = 0;
	        $scope.newJoinedEventsNum = 0;
	        $scope.creationData = {};
	        $scope.forms = {};
	        $scope.positiveIntRegex = "^[2-9][0-9]*$";
	        $scope.moneyRegex = "^\\$?[0-9][0-9\\,]*(\\.\\d{1,2})?$|^\\$?[\\.]([\\d][\\d]?)$";

	        $scope.getEventInfo = function() {
		        $.post("/events-view", {
			        userId : $rootScope.account.id
		        }, function(responseJSON) {
			        responseObject = JSON.parse(responseJSON);
			        console.log(responseObject);
			        $timeout(function() {
				        $scope.newMyEventsNum = responseObject.myEventNotifNum;
				        $scope.newJoinedEventsNum = responseObject.joinedEventNotifNum;
			        }, 0);
		        });
	        };

	        $scope.$on('$ionicView.enter', function() {
	        	console.log("enter");
		        $scope.getEventInfo();
	        });

	        $scope.goPending = function() {
		        $state.go('tab.pending-events');
	        };

	        $scope.goMyEvents = function() {
		        $state.go('tab.my-events');
	        };

	        $scope.goJoined = function() {
		        $state.go('tab.joined-events');
	        };

	        $scope.createEvent = function() {
		        $scope.closeModal();
		        var myPopup = $ionicPopup.show({
		          template : "<ion-spinner class='loader-center'></ion-spinner>",
		          title : 'Creating Event',
		          scope : $scope
		        });

		        $scope.creationData["owner_id"] = $rootScope.account.id;
		        $scope.creationData["tags"] = [];
		        $scope.lat = null;
		        $scope.lng = null;
		        function getLoc(callback) {
			        if (navigator.geolocation) {
				        var startPos;
				        var geoOptions = {
				          maximumAge : 2 * 1000,
				          timeout : 10 * 1000
				        }

				        var geoSuccess = function(position) {
					        startPos = position;
					        $scope.lat = startPos.coords.latitude;
					        $scope.lng = startPos.coords.longitude;

					        console.log("retrieved location");
					        console.log($scope.lat);
					        console.log($scope.lng);
					        callback();
				        };
				        var geoError = function(error) {
					        console.log('Error occurred. Error code: ' + error.code);
					        // error.code can be:
					        // 0: unknown error
					        // 1: permission denied
					        // 2: position unavailable (error response from location
					        // provider)
					        // 3: timed out
					        callback();
				        };

				        navigator.geolocation.getCurrentPosition(geoSuccess, geoError,
				            geoOptions);

			        } else {
				        console
				            .log('Geolocation is not supported for this Browser/OS version yet.');
				        callback();
			        }
		        }

		        function createEvent() {
			        console.log("STUFF");
			        console.log($scope.lat);
			        console.log($scope.lng);
			        $scope.creationData["lat"] = $scope.lat;
			        $scope.creationData["lng"] = $scope.lng;
			        console.log($scope.creationData);

			        $.post("/event-create", $scope.creationData, function(
			            responseJSON) {
				        console.log(responseJSON);
				        responseObject = JSON.parse(responseJSON);
				        console.log(responseObject);
				        $scope.creationData = {};
				        $scope.forms.eventCreate.$setPristine();
				        $scope.forms.eventCreate.$setUntouched();
			        });
			        myPopup.close();
			        $state.go('tab.my-events');
		        }
		        getLoc(createEvent);

		        // TODO: need to reset data?

	        }

	        $ionicModal.fromTemplateUrl('new-event-modal.html', {
	          scope : $scope,
	          animation : 'slide-in-up'
	        }).then(function(modal) {
		        $scope.modal = modal;
	        });
	        $scope.openModal = function() {
		        $scope.modal.show();
	        };
	        $scope.closeModal = function() {
		        $scope.modal.hide();
	        };
	        // Cleanup the modal when we're done with it!
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
	        /*
					 * if (!$rootScope.userId) { $location.path("/"); } var eventId =
					 * $routeParams.eventId; var postData = $.param({ json:
					 * JSON.stringify({ userId: userId, eventId: eventId }) });
					 * $http.post("/eventData", postData).success(function(data, status) {
					 * $scope.events = data.entries; });
					 */

        });