bulkAppControllers.controller("eventsCtrl", 
	function($scope, $rootScope, $http, $ionicModal, $state, $timeout) {
	
	$scope.newMyEvents = true;
	$scope.newMyEventsNum = 0; 
	$scope.newJoinedEvents = true;
	$scope.newJoinedEventsNum = 0; 
	$scope.newPendingEvents = false;
	$scope.newPendingEventsNum = 0; 
	$scope.creationData = {};
	
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
		$post("/event-create", $scope.creationData, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			console.log(responseObject);
		});
		var curData = $scope.creationData;
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